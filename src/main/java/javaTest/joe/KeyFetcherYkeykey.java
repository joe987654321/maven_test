package javaTest.joe;

import java.security.Key;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.oath.auth.Utils;
import com.yahoo.cryptogen.Credential;
import com.yahoo.cryptogen.credbank.CredBankException;
import com.yahoo.cryptogen.credbank.ykeykey.YKeyKeyBank;
import com.yahoo.parsec.config.ParsecConfig;
import com.yahoo.ykeykey.client.YKeyKeyClient;
import com.yahoo.ykeykey.client.YKeyKeyEnvironment;

public class KeyFetcherYkeykey {
    private final YKeyKeyBank ykeykeyBank;

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("trust_store_path", "/opt/yahoo/share/ssl/certs/yahoo_certificate_bundle.jks");
        map.put("trust_store_password", "changeit");
        map.put("athenz_service_cert_path", "/var/lib/sia/certs/ec-horizontal.rewards-dev.megabucks-beta.cert.pem");
        map.put("athenz_service_key_path", "/var/lib/sia/keys/ec-horizontal.rewards-dev.megabucks-beta.key.pem");
        map.put("keygroup", "ec-horizontal.megabucks.dev,ec-horizontal.megabucks.int");

        KeyFetcherYkeykey keyFetcherYkeykey = new KeyFetcherYkeykey(map);

        String [] keys = new String[] {
                "mrs.dev.ytwauctionservice.crumbkey",
                "ec-horizontal.megabucks.db-dev",
                "ec-horizontal.megabucks.db-int"
        };

        for (String key: keys) {
            try {
                String value = keyFetcherYkeykey.getKey(key);
                System.out.println(key + " is " + value);
            } catch (Exception e) {
                System.out.println("cannot get key " + key);
            }
        }
    }
    
    public KeyFetcherYkeykey(Map<String ,String> map) {
        final String trustStorePath = map.get("trust_store_path");
        final String trustStorePassword = map.get("trust_store_password");
        final String certPath = map.get("athenz_service_cert_path");
        final String keyPath = map.get("athenz_service_key_path");
        final String keyGroup = map.get("keygroup");
        try {

            SSLContext sslContext = buildSSLContext(trustStorePath, trustStorePassword, certPath, keyPath);
            YKeyKeyClient client = new YKeyKeyClient.YKeyKeyClientBuilder()
                    .environment(YKeyKeyEnvironment.aws)
                    .sslContext(sslContext)
                    .build();
            List<String> keyGroups = Arrays.asList(keyGroup.split(","));
            ykeykeyBank = new YKeyKeyBank(client, keyGroups);
            ykeykeyBank.start();
            ykeykeyBank.manualRefresh();
            ykeykeyBank.stop();
        } catch (Throwable t) {
            throw new RuntimeException("Exception raised when initialize key bank.", t);
        }
    }
    
    public String getKey(String keyName) {
        Credential cred;
        try {
            cred = ykeykeyBank.get(keyName);
        } catch (CredBankException e) {
            throw new RuntimeException("Exception raised when get credential from key bank with name:" + keyName, e);
        }
        //get the current version's secrete value
        if (cred.currentVersion.secret instanceof byte[]) {
            byte[] secrete = (byte[]) cred.currentVersion.secret;
            return new String(secrete);
        } else {
            throw new RuntimeException("Can not find secret of current version with name:" + keyName);
        }
    }

    private static SSLContext buildSSLContext(String trustStorePath, String trustStorePassword, String athensPublicKey,
            String athensPrivateKey)
            throws Exception {

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(Utils.getKeyManagers(athensPublicKey, athensPrivateKey),
                     getTruestManagers(trustStorePath, trustStorePassword), null);

        return context;
    }

    private static TrustManager[] getTruestManagers(String trustStorePath, String trustStorePassword) throws Exception {

        KeyStore keystore = Utils.getKeyStore(trustStorePath, trustStorePassword.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);
        return trustManagerFactory.getTrustManagers();
    }

}
