package javaTest.joe.MD5generator;

public class FetchKeyException extends RuntimeException {
    public FetchKeyException() {
        super();
    }

    public FetchKeyException(String message) {
        super(message);
    }

    public FetchKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
