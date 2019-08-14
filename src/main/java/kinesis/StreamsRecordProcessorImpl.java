package kinesis;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.streamsadapter.model.RecordAdapter;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class StreamsRecordProcessorImpl implements IRecordProcessor {
    private Integer checkpointCounter;

    private final AmazonDynamoDB dynamoDBClient;

    private static Logger logger = LoggerFactory.getLogger(StreamsRecordProcessorImpl.class);

    public StreamsRecordProcessorImpl(AmazonDynamoDB dynamoDBClient) {
        this.dynamoDBClient = dynamoDBClient;
    }

    @Override
    public void initialize(InitializationInput initializationInput) {
        checkpointCounter = 0;
    }


    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {

        logger.error(
                "Total size is "
                        + processRecordsInput.getRecords().size()
        );

        try {
            processRecordsInput.getCheckpointer().checkpoint();
        } catch (InvalidStateException | ShutdownException e) {
            e.printStackTrace();
        }
//
//        for (Record record : processRecordsInput.getRecords()) {
//            String data = new String(record.getData().array(), Charset.forName("UTF-8"));
//            System.out.println(data);
//            if (record instanceof RecordAdapter) {
//                com.amazonaws.services.dynamodbv2.model.Record streamRecord = ((RecordAdapter) record)
//                        .getInternalObject();
//                logger.error(
//                        "Total size is "
//                        + processRecordsInput.getRecords().size()
//                        + " get data: "
//                        + streamRecord.getDynamodb().getNewImage()
//                );
//            }
//            checkpointCounter += 1;
//
//            if (checkpointCounter % 10 == 0) {
//                try {
//                    processRecordsInput.getCheckpointer().checkpoint();
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
            try {
                shutdownInput.getCheckpointer().checkpoint();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}