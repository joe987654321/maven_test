package javaTest.joe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class CipherTest {
    public static void main(String[] args)
            throws DecoderException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        //String credentialStr = "uOb+zD8m9VWMp/13irlYHw==";
        //String credentialStr = "aOATUD+epXWQKKczGYIwIA==";

        File f = new File("ecouponIdToCampaignId.tgz.enc");
        String cipherText = FileUtils.readFileToString(f, Charset.forName("UTF-8"));
        cipherText = cipherText.replaceAll("\n+", "");
        //String password = "667d5394fb2b707b6d8db3e87aacfbca";
        String password = "0eebf2cd482d63d5850a56a76bba6cfe5b21c7e59744e3d519fcd80b331eca72";
        byte[] key = Hex.decodeHex(password.toCharArray());
        byte[] secret = Base64.getDecoder().decode(cipherText.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] tgzBytes = cipher.doFinal(secret);

        TarInputStream zis = new TarInputStream(new GZIPInputStream(new ByteArrayInputStream(tgzBytes)));
        TarEntry ze = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        final String content;
        if (ze != null) {
            StringBuilder sb = new StringBuilder();
            int readSize;
            while ((readSize = zis.read(buffer)) > 0) {
                if (readSize < 1024) {
                    sb.append(new String(Arrays.copyOf(buffer, readSize), Charset.forName("UTF-8")));
                } else {
                    sb.append(new String(buffer, Charset.forName("UTF-8")));
                }
            }
            content = sb.toString();
            System.out.println(content.substring(1, 1000));
        } else {
            throw new RuntimeException("read content failed");
        }

//        FileUtils.writeStringToFile(new File("testout.txt"), content, Charset.forName("UTF-8"));

        Map<Integer, Integer> ecouponIdToCampaignId = new HashMap<>();
        Map<String, Integer> activityIdWithUserIdToEcouponId = new HashMap<>();

        try (Scanner scanner = new Scanner(content)) {
            while (scanner.hasNextLine()) {
                // ecoupon_id activity_id user_id
                String[] line = scanner.nextLine().split(" ");
                try {
                    ecouponIdToCampaignId.put(Integer.valueOf(line[0]), Integer.valueOf(line[1]));
                    activityIdWithUserIdToEcouponId.put(line[1] + "_" + line[2], Integer.parseInt(line[0]));
                } catch (Exception e) {
                    System.out.println(Arrays.toString(line));
                    throw e;
                }
            }
        }

        //556277999 95110 flower.0630#oa
        System.out.println(ecouponIdToCampaignId.get(556277999));
        System.out.println(activityIdWithUserIdToEcouponId.get("95110_flower.0630#oa"));

    }

}
