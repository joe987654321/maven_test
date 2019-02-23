package javaTest.joe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import yjava.ycore.yax.YAX;
import yjava.ycore.yax.YAXException;
import yjava.ycore.yax.YAXProvider;

public class BBB {
    public static void main(String[] args) throws YAXException, IOException {
//        final YAX yax = YAXProvider.createYax(YAXProvider.YAXType.JAVA);
//        System.out.println(yax.fromY64("YXBwaWQ9ZGV2"));
//
//
//        long initDelay = (86400 - System.currentTimeMillis() % 86400 + 300) % 86400;

        BufferedImage inputImage = ImageIO.read(new File("/Users/joe321/Downloads/fail.png"));

        if (inputImage.getColorModel().hasAlpha()) {
            inputImage = dropAlphaChannel(inputImage);
        }


        System.out.println(inputImage.getType());

        File outputFile = File.createTempFile("imageuploader2-", "");

        System.out.println(outputFile.getAbsolutePath());

        ImageIO.write(inputImage, "jpg", outputFile);

    }

    private static BufferedImage dropAlphaChannel(BufferedImage src) {
        BufferedImage convertedImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImg.getGraphics().drawImage(src, 0, 0, Color.WHITE, null);

        return convertedImg;
    }

}
