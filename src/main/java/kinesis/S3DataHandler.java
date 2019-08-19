package kinesis;

import java.awt.Event;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Tag;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class S3DataHandler {

    public static Logger logger = LoggerFactory.getLogger(S3DataHandler.class);
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();
    private static final long UPLOAD_PERIOD = 3600; //1 hr
    private long startTimeOfDataHandling;
    private boolean forceUploadS3Once = false;
    private Map<Long, Map<String, RememberDataSegment>> cachedDatum = new HashMap<>();
    private AmazonS3 s3Client;

    S3DataHandler(long startTimeOfDataHandling, AWSCredentialsProvider provider) {
        this.startTimeOfDataHandling = startTimeOfDataHandling;

        s3Client = AmazonS3Client.builder()
                .withRegion("ap-southeast-1")
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTPS).withMaxConnections(10))
                .withCredentials(provider)
                .build();
    }

    void handleData(EventType eventType, StreamRecord streamRecord) {
        if (EventType.REMOVE.equals(eventType)) {
            return;
        }
        long recordTime = TimeUnit.SECONDS.convert(
                streamRecord.getApproximateCreationDateTime().getTime(),
                TimeUnit.MILLISECONDS
        );
        if (recordTime >= startTimeOfDataHandling) {
            synchronized (this) {
                if (recordTime > startTimeOfDataHandling) {
                    if (recordTime > startTimeOfDataHandling + UPLOAD_PERIOD) {
                        startTimeOfDataHandling = startTimeOfDataHandling
                                + (recordTime - startTimeOfDataHandling) / UPLOAD_PERIOD * UPLOAD_PERIOD;
                    }
                    cachedDatum.putIfAbsent(startTimeOfDataHandling, new HashMap<>());

                    String rId = streamRecord.getKeys().get("rId").getS();
                    String appId = streamRecord.getKeys().get("appId").getS();

                    String ridAndAppId = rId + "__" + appId;
                    cachedDatum.get(startTimeOfDataHandling).putIfAbsent(ridAndAppId,
                            new RememberDataSegment().withRid(rId).withAppId(appId).withUpdatedTs(recordTime)
                    );

                    if (EventType.INSERT.equals(eventType)) {
                        Map<String, AttributeValue> newDatum = streamRecord.getNewImage().get("datum").getM();
                        newDatum.forEach((k, v) -> cachedDatum.get(startTimeOfDataHandling).get(ridAndAppId).getDatum()
                                .put(k, JSON_PARSER.parse(v.getS())));
                    } else if (EventType.MODIFY.equals(eventType)) {
                        Map<String, AttributeValue> newDatum = streamRecord.getNewImage().get("datum").getM();
                        Map<String, AttributeValue> oldDatum = streamRecord.getOldImage().get("datum").getM();
                        newDatum.forEach((k, v) -> {
                            if (!v.equals(oldDatum.get(k))) {
                                cachedDatum.get(startTimeOfDataHandling).get(ridAndAppId).getDatum()
                                        .put(k, JSON_PARSER.parse(v.getS()));
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean isPreparingData() {
        return !cachedDatum.isEmpty();
    }

    public boolean readyUploadingToS3() {
        return cachedDatum.size() > 1 || forceUploadS3Once;
    }

    public void setForceUploadS3() {
        logger.error("set forceUploadS3Once");
        forceUploadS3Once = true;
    }

    public void uploadDataToS3() throws RuntimeException {
        //always upload data
        synchronized (this) {
            //upload to s3
//            logger.error("fake upload to s3 message:");
//            cachedDatum.forEach((time, segments) -> {
//                logger.error("time is "+ time);
//                segments.values().forEach(rememberDataSegment -> {
//                    logger.error("guid is " + rememberDataSegment.rId + ", appId is " + rememberDataSegment.appId);
//                    logger.error("datum is " + rememberDataSegment.datum);
//                });
//            });

            cachedDatum.forEach((time, segments) -> {
                try {
                    File file = File.createTempFile("s3image-", "");
                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.write(GSON.toJson(segments.values()));
                    }

                    String bucketName = "ec-horizontal-remember-data-pipe-int";
                    String s3FileName = "working/" + "dev" + "/" + time + "/rememberEvent-" + System.currentTimeMillis();
                    int delayDeleteDays = 7;
                    uploadFileToS3Bucket(file, bucketName, s3FileName, delayDeleteDays);
                    logger.info("success upload to s3");
                } catch (IOException e) {
                    logger.error("cannot upload to s3, file io exception", e);
                    throw new RuntimeException(e);
                }
            });

            cachedDatum.clear();
            forceUploadS3Once = false;
        }
    }

    private void uploadFileToS3Bucket(final File file,
            final String bucketName, final String s3key, final int delayDeleteDays) throws RuntimeException
    {
        PutObjectRequest req = new PutObjectRequest(bucketName, s3key, file);
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType("application/json");
        req.setMetadata(metadata);
        if (delayDeleteDays > 0) {
            req.setTagging(new ObjectTagging(
                    Collections.singletonList(new Tag("AUTO_DELETE_AFTER", delayDeleteDays + "d"))));
        }
        // upload to s3
        try {
            s3Client.putObject(req);
        } catch (SdkClientException e) {
            logger.error("cannot upload to s3, aws sdk client exception", e);
            throw new RuntimeException(e);
        }
    }
}
