package javaTest.joe.MD5generator;

import yjava.security.ysecure.YCR;
import yjava.security.ysecure.YCRException;

public class MD5generatorYCRImpl implements IMD5generator {
    @Override public String signMd5Compute(String stringToSign, String keyName) {
        //final String keyname = "mrs." + appid + ".crumbkey";
        final YCR ycr;
        try {
            ycr = YCR.createYCR();
            return ycr.signMD5Compute(stringToSign, keyName);
        } catch (YCRException e) {
            throw new FetchKeyException("Cannot fetch key with YCR", e);
        }

    }
}
