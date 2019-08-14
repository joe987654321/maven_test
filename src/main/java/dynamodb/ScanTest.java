package dynamodb;

import java.util.Collections;
import java.util.Map;
import java.util.TimeZone;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.yahoo.nevec.credential.provider.NevecAwsCredentialsProviderFactory;

public class ScanTest {

    private static DynamoDBMapper mapper;
    private static DynamoDBMapperConfig dynamoDBMapperConfig;
    private static Table table;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        ClientConfiguration clientConfiguration = new ClientConfiguration().withTcpKeepAlive(true);
        String credentialPath = "/tmp/uid.aws_temp_credential";

        AmazonDynamoDBClientBuilder clientBuilder = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-southeast-1")
                .withClientConfiguration(clientConfiguration);
        AWSCredentialsProvider provider = new NevecAwsCredentialsProviderFactory(
                Collections.singletonList(credentialPath)).getProvider(credentialPath);

        AmazonDynamoDB client = clientBuilder.withCredentials(provider).build();

        String tableName = "Dev_Remember";
        DynamoDBMapperConfig.Builder builder = DynamoDBMapperConfig.builder();
        builder.setTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName));
        dynamoDBMapperConfig = builder
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES).build();
        mapper = new DynamoDBMapper(client, dynamoDBMapperConfig);
        table = new DynamoDB(client).getTable(tableName);
    }


    public static void main(String[] args) {
        new ScanTest().scan(1562572805L);

    }

    public void scan(Long timestamp) {
//        Condition condition = new Condition()
//                .withComparisonOperator(ComparisonOperator.GT.toString())
//                .withAttributeValueList(new AttributeValue().withN(Long.toString(timestamp)));
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withN(Long.toString(timestamp)));
        Map<String, Condition> scanFilter = Collections.singletonMap("updatedTs", condition);
        DynamoDBScanExpression expression = new DynamoDBScanExpression().withIndexName("updatedTsIndex").withScanFilter(scanFilter);
        PaginatedScanList<RememberDynamodb> scanResult = mapper.scan(RememberDynamodb.class, expression);

        System.out.println("rId appId datum");
        for (RememberDynamodb result : scanResult) {
            System.out.print(result.getrId() + " ");
            System.out.print(result.getAppId() + " ");
        }

    }


}
