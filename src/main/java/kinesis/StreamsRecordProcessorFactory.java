package kinesis;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;

public class StreamsRecordProcessorFactory implements IRecordProcessorFactory {
    private final S3DataHandler s3DataHandler;

    public StreamsRecordProcessorFactory(S3DataHandler s3DataHandler) {
        this.s3DataHandler = s3DataHandler;
    }

    @Override
    public IRecordProcessor createProcessor() {
        return new StreamsRecordProcessorImpl(s3DataHandler);
    }
}