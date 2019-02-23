package javaTest.joe.MD5generator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class MD5generatorPureJavaImpl implements IMD5generator {

    final KeyFetcher keyFetcher;

    public MD5generatorPureJavaImpl(KeyFetcher keyFetcher) {
        this.keyFetcher = keyFetcher;
    }

    public static interface KeyFetcher {
        String fetchKey(String keyName) throws FetchKeyException;
    }

    @Override
    public String signMd5Compute(String stringToSign, String keyName) throws FetchKeyException {
        // md5 sign str + key
        // convert to y64
        String toSign = stringToSign + keyFetcher.fetchKey(keyName);
        String signedStr = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(toSign.getBytes());
            signedStr = y64( thedigest );
        } catch (NoSuchAlgorithmException e) {
            // should never get here since MD5 is valid
        }
        return signedStr;
    }

    private static String y64(byte[] str) {
        return Base64.encodeBase64String(str).replace( '+', '.').replace( '=', '-').replace('/','_');
    }
}
