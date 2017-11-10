package zts;

import com.yahoo.auth.Principal;
import com.yahoo.auth.util.PrincipalAuthority;
import com.yahoo.auth.util.SimplePrincipal;
import com.yahoo.auth.zts.ZTSClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ZtsClientTesting {

    public static void main(String[] args) throws IOException {

        String ntokenPath = "/tmp/ntoken";
        String reqDomain = "ybybbb";
        String reqServiceName = "joe321";
        String ntoken = new String(Files.readAllBytes(Paths.get(ntokenPath))).trim();
        Principal principal = SimplePrincipal.create(reqDomain, reqServiceName, ntoken, new PrincipalAuthority());
        System.out.println(principal.getAuthorizedService());
      //  String ztoken = new ZTSClient("https://zts.athens.yahoo.com:4443", principal).getRoleToken("nevec.apex.external").getToken();
      //  System.out.println(ztoken);

    }
}
