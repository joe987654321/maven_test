package javaTest.joe.MD5generator;

public interface IMD5generator {
    String signMd5Compute(String stringToSign, String keyname) throws FetchKeyException;
}
