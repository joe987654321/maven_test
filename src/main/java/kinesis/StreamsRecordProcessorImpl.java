package kinesis;

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

public class StreamsRecordProcessorImpl implements IRecordProcessor {

    private final S3DataHandler s3DataHandler;

    private static Logger logger = LoggerFactory.getLogger(StreamsRecordProcessorImpl.class);

    public StreamsRecordProcessorImpl(S3DataHandler s3DataHandler) {
        this.s3DataHandler = s3DataHandler;
    }

    @Override
    public void initialize(InitializationInput initializationInput) {

    }


    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {
        logger.error(
                "Total size is "+ processRecordsInput.getRecords().size()
        );

        for (Record record : processRecordsInput.getRecords()) {
            if (record instanceof RecordAdapter) {
                com.amazonaws.services.dynamodbv2.model.Record record1 = ((RecordAdapter) record)
                        .getInternalObject();

                s3DataHandler.handleData(EventType.valueOf(record1.getEventName()), record1.getDynamodb());

            //    com.amazonaws.services.kinesis.clientlibrary.lib.worker.BlockOnParentShardTask;
            }
        }
        if (!s3DataHandler.isPreparingData() || s3DataHandler.readyUploadingToS3()) {
            if (s3DataHandler.readyUploadingToS3()) {
                s3DataHandler.uploadDataToS3();
            }

            try {
                processRecordsInput.getCheckpointer().checkpoint();
            } catch (InvalidStateException | ShutdownException e) {
                logger.warn("cannot set checkpoint after upload data to s3", e);
                //fail to set check point is not critical issue, our data can be upload multi times
            }
        }
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
            s3DataHandler.uploadDataToS3();
            try {
                shutdownInput.getCheckpointer().checkpoint();
            } catch (InvalidStateException | ShutdownException e) {
                logger.warn("cannot set checkpoint after receive shutdown request", e);
                //fail to set check point is not critical issue, our data can be upload multi times
            }
        }
    }
}