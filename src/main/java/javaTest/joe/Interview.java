package javaTest.joe;

import org.junit.Assert;

public class Interview {

    public static void main(String[] args) {
        Assert.assertTrue(compress("").equals(""));
    }

    public static   String compress(String inputStr) {

        String compressing = "";

        for (int i = 0, count = 1; i < inputStr.length(); i++) {

            if (i + 1 < inputStr.length() && inputStr.charAt(i) == inputStr.charAt(i + 1))
                count++;
            else {
                compressing = compressing.concat(Character.toString(inputStr.charAt(i))).concat(Integer.toString(count));
                count = 1;
            }

        }

        return compressing;

    }

}
