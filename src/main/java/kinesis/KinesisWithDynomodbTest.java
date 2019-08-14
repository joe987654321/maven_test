package kinesis;

import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsClientBuilder;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.streamsadapter.AmazonDynamoDBStreamsAdapterClient;
import com.amazonaws.services.dynamodbv2.streamsadapter.StreamsWorkerFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.yahoo.nevec.credential.provider.NevecAwsCredentialsProviderFactory;

public class KinesisWithDynomodbTest {

    private static AmazonDynamoDB dynamoDBClient;
    private static AmazonCloudWatch cloudWatchClient;
    private static AmazonDynamoDBStreams dynamoDBStreamsClient;
    private static AmazonDynamoDBStreamsAdapterClient adapterClient;

    private static Regions awsRegion = Regions.AP_SOUTHEAST_1;

    private static String credentialPath = "/tmp/uid.aws_temp_credential";

    private static AWSCredentialsProvider provider;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        provider = new NevecAwsCredentialsProviderFactory(
                Collections.singletonList(credentialPath)).getProvider(credentialPath);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting demo...");

        dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(provider)
                .build();
        cloudWatchClient = AmazonCloudWatchClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(provider)
                .build();
        dynamoDBStreamsClient = AmazonDynamoDBStreamsClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(provider)
                .build();
        adapterClient = new AmazonDynamoDBStreamsAdapterClient(dynamoDBStreamsClient);

        //        String latestStreamArn = "arn:aws:dynamodb:ap-southeast-1:690060915618:table/Dev_Remember/stream/2019-08-05T08:16:29.888";
        String latestStreamArn = describeTable(dynamoDBClient, "Dev_Remember").getTable().getLatestStreamArn();
        System.out.println(latestStreamArn);

//        String srcTable = tablePrefix + "-src";
//        String destTable = tablePrefix + "-dest";
        StreamsRecordProcessorFactory recordProcessorFactory = new StreamsRecordProcessorFactory(dynamoDBClient);

//        setUpTables();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();


        KinesisClientLibConfiguration workerConfig = new KinesisClientLibConfiguration("streams-adapter-demo",
                latestStreamArn,
                provider,
                "streams-demo-worker")
                .withMaxRecords(10000)
                .withIdleTimeBetweenReadsInMillis(500)
                .withTimestampAtInitialPositionInStream(yesterday);

        System.out.println("Creating worker for stream: " + latestStreamArn);
        Worker worker = StreamsWorkerFactory
                .createDynamoDbStreamsWorker(recordProcessorFactory, workerConfig, adapterClient, dynamoDBClient,
                        cloudWatchClient);
        System.out.println("Starting worker...");
        Thread t = new Thread(worker);
        t.start();

        while (true) {
            Thread.sleep(10000);
        }
//        worker.shutdown();
//        t.join();
//
//        System.out.println("Done.");
    }


    public static DescribeTableResult describeTable(AmazonDynamoDB client, String tableName) {
        return client.describeTable(new DescribeTableRequest().withTableName(tableName));
    }
}
