package javaTest.joe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

import org.apache.commons.codec.binary.Base64;

public class DecodeBase64Example {
    public static void main(String[] args) throws IOException{
        String testinstring = "test compressed text";

        //String compressedString = z64(testinstring);
        //System.out.println(compressedString);

        //String testoutstring;
        //testoutstring = decompress(compressedString);
        //System.out.println(testoutstring);

        String compress_string = "eJyVVktv4zYQ/iuBet0Ekmw5do5NF0UPiy26KYq2KAiaGktEKFIhKTvaIP99h5RES5ZVID4Y4jdDzvvxFqkaNLVcyeghMq1k0aeoAktJTi2NHt4iCUdgxGhGSmUsMu1vK9BQmCS9A3ZnT8ldS0ul7piq8G7HbnkFxtKqRv4kSzarbBunm2yXre83gcm0xkKFHF9As5LKnBu4+fzrN6cBz93NGH+bZL1exVsEjQfd/6eI+u/uncaAJhnNskMc79k9S5KfVIk8tXa22XZgRIgxlQOe7Sl6RymmcBZeFcYrWoCJHmQjxMdFV84Bve3pZofPOv0ttQ0+uULVzvQs2ybrLPGX2tqBey/m/7XHQ6M1SOboT3/9goCSI6HJdpV6oepwuKZLrTmDwTqoKBfDoS6VDJS2Gr4YtYRaq53D/vj69Qt5+vv3z/gqJsLN0WnvwW+//YPgv1G6QmeSAJGfkeESe4z+e3dmGEvwcSiUboMwB3YJOAIOHESezKF0Dq3m0HoOZQG6Gg5qDJlp4h1Hukh1CJc5vM4ZR/BU8UtCukRYLRHWS4SzQUIZIIaVkDcCUxQTSNqBqKHCi6TjkcryQ7vEKqgVShZBXlUQzMsDF10aEmBxfBujs/KmayLoApeRSZd5pgRxRG8pVzGb2DE6T3/3GRb9+fToyqmximjoWZlqnPTofkahQqgTdJUaBSN69fG7sSEi/Q1eD4BVlgqyLKmjz0hOjtK8IEPldq95KNTVUB6SgSBjKAcBFiZQjT2FHDmcBkCqQqum7mUdQXOMY+7byuwOOQE8i3ZUkH3NRIn74f0TtawUHLNhyhrwAXiR9IIFkeGT1rXi0laYBhdMI0rQTtWNoJrbwKSpRIVdJSRzKJ1DIcsttz6tqtE0cN3F4iS58cSbg9I3T4/rXbaLxk+s56+GSjDPvCbn5jWktSwa9CtK+17eojCL7/leXo3DxaWrhVFf8hk0JHXXrP2kq6mMLqijZzw+qoZLUo8vlGAOhpkltzhip7rP0fAujhDixxS2XBxsuyTdbXe+7YOhAplLXtdcFpNGNiMqmY/enJHpwYLGeg+FYemrkqpqsSpz8PKHboQdCu0aIa5lnY+DExe9d8mw1K4UG1aZ8InC/ITx9Yldg+rnoMWZsA8x9oOfNFpc+ubIVWPIpWfj+13sPGs1ZTAZACU1paXFKCRudvr7fu15W4wSvHbOmS1AoI9+bF/fCEYbwyhZ/D0hevf3O4zTgEhafaTeuib50mC1+HL3TR6zTlvSbxNRuruLu+bsVAUy2TL2TSvVaYp116/tLYDN/dri4tMBHQTS+ECP5x1QLQbFvIVD346ysDDNle3scrHwWdb34iBiMg24IV1b788lL0p0l7udTxN8Pg+YBhrmwdTYSuVuil2zVygcP/Aycfc5+8c1Np34Z4pEt5Bl8nl7CHVMj7gP0j3W+kW0ccwSyhjUcy/67jlyYj950H9z35zLbuKfD5VpDgfAeA5rjJM8DlQX1BPqezYhdh0do9DNryiNnebv+PsBx11u8Q==";
        String output = normal_decompress(compress_string);
        System.out.println(output);
        return;
    }

    public static String z64(String s) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        dos.write(s.getBytes("UTF-8"));
        dos.close();
        baos.close();
        byte [] b =  baos.toByteArray();
        return "--gz_base64--"+Base64.encodeBase64String(b);

    }

    public static String decompress(String s) throws IOException{
        while (true){
            if (s.startsWith("--gz_base64--")){
                System.out.println(s+"####");
                s = s.substring("--gz_base64--".length());
                System.out.println(s+"####");
                byte[] decodeBase64 = Base64.decodeBase64(s.getBytes("UTF-8"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    InflaterOutputStream ios = new InflaterOutputStream(baos);
                    try {
                        ios.write(decodeBase64);
                        s = new String(baos.toByteArray(),"UTF-8");
                    } finally {
                        ios.close();
                    }
                } finally {
                    baos.close();
                }
            }
            else
                break;
        }
        return s;
        /*
        byte[] decodeBase64 = Base64.decodeBase64(s);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InflaterOutputStream ios = new InflaterOutputStream(baos);
            try {
                ios.write(decodeBase64);
                return new String(baos.toByteArray(), "UTF-8");
            } finally {
                ios.close();
            }
        } finally {
            baos.close();
        }
        */
    }

    public static String normal_decompress(String s) throws IOException{

        byte[] decodeBase64 = Base64.decodeBase64(s);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InflaterOutputStream ios = new InflaterOutputStream(baos);
            try {
                ios.write(decodeBase64);
                return new String(baos.toByteArray(), "UTF-8");
            } finally {
                ios.close();
            }
        } finally {
            baos.close();
        }


    }
}


