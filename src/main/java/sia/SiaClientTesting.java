package sia;

import com.yahoo.auth.Principal;
import com.yahoo.auth.sia.SIAClient;

import java.io.IOException;

public class SiaClientTesting {


    public static void main(String[] args) {
        SIAClient siaClient = new SIAClient();
        try {
            Principal local_test = siaClient.getServicePrincipal("nevec.apex", "local_test", 30 * 60, 4 * 60 * 60, true);
            System.out.println("authority is " + local_test.getAuthority());
            System.out.println("ntoken is " + local_test);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
