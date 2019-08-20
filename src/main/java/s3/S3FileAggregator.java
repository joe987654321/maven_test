package s3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListBucketsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yahoo.nevec.credential.provider.NevecAwsCredentialsProviderFactory;

import kinesis.RememberDataSegment;


public class S3FileAggregator {
    //private static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    public static Logger logger = LoggerFactory.getLogger(S3FileAggregator.class);
    private static final String credentialPath = "/tmp/uid.aws_temp_credential";
    private static AWSCredentialsProvider provider;
    private AmazonS3 s3Client;
    private Gson GSON = new Gson();
    private String bucketName;
    private String env;
    private int delayDeleteDays;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        provider = new NevecAwsCredentialsProviderFactory(
                Collections.singletonList(credentialPath)).getProvider(credentialPath);
    }

    public static void main(String[] args) {
        S3FileAggregator s3FileAggregator = new S3FileAggregator();

//        while (true) {
//            try {
//                ses.awaitTermination(24, TimeUnit.HOURS);
//            } catch (InterruptedException e) {
//                //do nothing
//            }
//        }
    }

    S3FileAggregator() {
        s3Client = AmazonS3Client.builder()
                .withRegion("ap-southeast-1")
                .withClientConfiguration(
                        new ClientConfiguration().withProtocol(Protocol.HTTPS).withMaxConnections(10))
                .withCredentials(provider)
                .build();
        bucketName = "ec-horizontal-remember-data-pipe-int";
        env = "dev";
        delayDeleteDays = 7;

        this.generateReport();
    //    ses.scheduleAtFixedRate(this::aggregateFiles, 0, 60, TimeUnit.SECONDS);
    }

    public void generateReport() {
        Calendar cal = Calendar.getInstance();
        long fromTime = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();
        //cal.add(Calendar.DATE, -1);
        String content = aggregateFiles(fromTime);
        String dateFormat = simpleDateFormat.format(cal.getTime());
        if (content != null) {
            uploadManifestFile(dateFormat);
            uploadDailyReport(content, dateFormat);
        }
    }

    private String aggregateFiles(long fromTime) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
                .withPrefix("working/" + env + "/");
        List<String> segmentFileNames = new ArrayList<>();

        ListObjectsV2Result listObjectsV2Result;
        do {
            listObjectsV2Result = s3Client.listObjectsV2(listObjectsV2Request);
            System.out.println("All files");
            System.out.println("---------------------");
            listObjectsV2Result.getObjectSummaries().forEach(s3ObjectSummary -> {
                System.out.println(s3ObjectSummary.getKey());
            });

            segmentFileNames.addAll(fetchSegmentFileNames(listObjectsV2Result.getObjectSummaries(), fromTime, fromTime + 86400));

            listObjectsV2Request.setContinuationToken(listObjectsV2Result.getNextContinuationToken());
        } while (listObjectsV2Result.isTruncated());

        segmentFileNames.sort(String::compareTo); //sort by name, file name include timestamp

        System.out.println("to be process file names");
        System.out.println("---------------------");
        segmentFileNames.forEach(System.out::println);

        if (segmentFileNames.isEmpty()) {
            //no segment files, skip
            logger.warn("no segment file found, skip aggregation");
            return null;
        }

        Map<String, RememberDataSegment> aggregatedContents = new HashMap<>();

        //List<String> contents = new ArrayList<>();
        segmentFileNames.forEach(segmentFileName -> {
            String content = s3Client.getObjectAsString(bucketName, segmentFileName);
            List<RememberDataSegment> rememberDataSegments = GSON.fromJson(
                    content, new TypeToken<List<RememberDataSegment>>() {}.getType()
            );

            rememberDataSegments.forEach( rds -> {
                String rId = rds.getrId();
                String appId = rds.getAppId();
                Long recordTime = rds.getUpdatedTs();
                String ridAndAppId = rId + "__" + appId;
                aggregatedContents.putIfAbsent(
                        ridAndAppId,
                        new RememberDataSegment().withRid(rId).withAppId(appId).withUpdatedTs(recordTime)
                );

                rds.getDatum().forEach((k, v) -> aggregatedContents.get(ridAndAppId).getDatum()
                        .put(k, v));
            });

        });

        System.out.println(GSON.toJson(aggregatedContents.values()));

        return GSON.toJson(aggregatedContents.values());
    }

    private List<String> fetchSegmentFileNames(List<S3ObjectSummary> s3ObjectSummaries, long fromTime, long toTime) {
        List<String> toBeProcessS3ObjectSummaryKeys = new ArrayList<>();
        s3ObjectSummaries.forEach(s3ObjectSummary -> {
            String[] splits = s3ObjectSummary.getKey().split("/");
            long segmentDirTime = Long.parseLong(splits[2]);
            if (segmentDirTime >= fromTime && segmentDirTime < toTime) {
                toBeProcessS3ObjectSummaryKeys.add(s3ObjectSummary.getKey());
            }
        });
        return toBeProcessS3ObjectSummaryKeys;
    }

    private void uploadManifestFile(String dateFormat) {
        String dailyReportPath = "s3a://" + bucketName + "/" + env + "/" + dateFormat + "/rememberReport";
        String content = "{\"entries\": [{\"url\":\"" + dailyReportPath + "\"}]}";
        createFileAndUpload(content, ".MANIFEST", dateFormat);
    }

    private void uploadDailyReport(String content, String dateFormat) {
        createFileAndUpload(content, "rememberReport", dateFormat);
    }

    private void createFileAndUpload(String content, String fileName, String dateFormat) {
        try {
            File tmpFile = File.createTempFile("tmp-" + fileName, "");
            try (FileWriter fileWriter = new FileWriter(tmpFile)) {
                fileWriter.write(content);
            }
            uploadFileToS3Bucket(tmpFile, bucketName, env + "/" + dateFormat + "/" + fileName, delayDeleteDays);
        } catch (IOException e) {
            throw new RuntimeException("cannot create/write manifestFile", e);
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
