package s3;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListBucketsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yahoo.nevec.credential.provider.NevecAwsCredentialsProviderFactory;

import kinesis.RememberDataSegment;

public class S3FileAggregator {
    //private static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private static final String credentialPath = "/tmp/uid.aws_temp_credential";
    private static AWSCredentialsProvider provider;
    private AmazonS3 s3Client;
    private Gson GSON = new Gson();

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
        this.aggregateFiles();
    //    ses.scheduleAtFixedRate(this::aggregateFiles, 0, 60, TimeUnit.SECONDS);
    }

    private void aggregateFiles() {
        String bucketName = "ec-horizontal-remember-data-pipe-int";
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
                .withPrefix("working/dev/");
        ListObjectsV2Result listObjectsV2Result = s3Client.listObjectsV2(listObjectsV2Request);

//        Calendar cal = Calendar.getInstance();
//        long today = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();
//        cal.add(Calendar.DATE, -1);
//        long yesterday = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();

        Calendar cal = Calendar.getInstance();
        long yesterday = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();
        cal.add(Calendar.DATE, 1);
        long today = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();

        System.out.println("All files");
        System.out.println("---------------------");
        listObjectsV2Result.getObjectSummaries().forEach(s3ObjectSummary -> {
            System.out.println(s3ObjectSummary.getKey());
        });

        List<String> segmentFileNames = fetchSegmentFileNames(
                listObjectsV2Result.getObjectSummaries(), yesterday, today);

        segmentFileNames.sort(String::compareTo);

        System.out.println("to be process file names");
        System.out.println("---------------------");
        segmentFileNames.forEach(System.out::println);


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
    }

    private List<String> fetchSegmentFileNames(List<S3ObjectSummary> s3ObjectSummaries, long fromTime, long toTime) {
        List<String> toBeProcessS3ObjectSummaryKeys = new ArrayList<>();
        s3ObjectSummaries.forEach(s3ObjectSummary -> {
            String[] splits = s3ObjectSummary.getKey().split("/");
            long segmentDirTime = Long.parseLong(splits[2]);
            if (segmentDirTime >= fromTime && segmentDirTime < toTime) {
                toBeProcessS3ObjectSummaryKeys.add(s3ObjectSummary.getKey());
            }
            System.out.println(s3ObjectSummary.getKey());
        });
        return toBeProcessS3ObjectSummaryKeys;
    }

    //Map<Long, Map<Long, RememberDataSegment>> rememberDataSegments = new HashMap<>();
    //Map<Long, Map<Long, RememberDataSegment>>
}
