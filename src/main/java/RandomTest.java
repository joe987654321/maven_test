import java.security.SecureRandom;
import java.util.Random;

public class RandomTest {

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();

        for (int i = 0; i < 10000000; i++) {
            getRandom();
        }

        long t2 = System.currentTimeMillis();

        for (int i = 0; i < 10000000; i++) {
            getSecureRandom();
        }

        long t3 = System.currentTimeMillis();

        System.out.println("Normal random: " + (t2 -t1));
        System.out.println("Secure random: " + (t3 -t2));
    }

    private static int getRandom() {
        Random random = new Random();
        //random.setSeed(1);
        int a = random.nextInt(2);
        return a;
    }

    private static int getSecureRandom() {
        SecureRandom random = new SecureRandom();
        //random.setSeed(1);
        int a = random.nextInt(2);
        return a;
    }
}
