package bastet

import groovy.json.JsonOutput
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import spock.lang.Specification

import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import com.oath.auth.Utils

class AthenzTest extends Specification {

    private static def WITHOUT_USER_AUTH_CERT_PATH = "/Users/joe321/partial_mask_role_cert.pem"
    private static def SERVICE_KEY_PATH = "/Users/joe321/functional_test_service.key"


    def "test athenz"() {
        //Given I create an einvoiceCarrier object with "<userId>"
        //When I insert the einvoiceCarrier with auth type "WITHOUT_USER_AUTH_CERT"
        when:
        def serviceName = "einvoiceCarrier"
        def object = new EinvoiceCarrier()
        object.userId = "y-TCW22FSP4BRN6IJMYEXVZQYFX7"

        def parameter = [:]
        parameter.authType = "WITHOUT_USER_AUTH_CERT"

        then:
        println "GREAT"
        def resp = sendCustQuery("post", "/api/einvoice-carrier/v1/einvoiceCarriers", object, parameter)
        println "response is: "
        println JsonOutput.toJson(resp.data)

    }

    def sendCustQuery(def method, def path, def object, def parameter) {

        def client = new RESTClient("https://b-bastet1.ec.tw1.yahoo.com:4443");

        if (parameter != null) {
            if (parameter.containsKey("authType")) {
                if (parameter.authType == "WITHOUT_USER_AUTH_CERT") {
                    SSLContext sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(getKeyManagers(WITHOUT_USER_AUTH_CERT_PATH), getAllPassTruestManagers(), null)
                    SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext, new AllowAllHostnameVerifier())
                    Scheme scheme = new Scheme("https", 4443, socketFactory)

                    client.client.getConnectionManager().getSchemeRegistry().register(scheme)
                    //client.ignoreSSLIssues()
                } else {
                    throw new RuntimeException("unknown authType")
                }
                parameter.remove("authType")
            }
            if (parameter.containsKey("accept")) {
                client.headers.accept = parameter."accept"
                parameter.remove("accept")
            }
        }


        client.handler.failure = { resp, reader ->
            resp.data = reader
            resp
        }
        client.handler.success = { resp, reader ->
            resp.data = reader
            resp
        }

        def queryMap = [
                path: path,
                query: parameter,
                requestContentType: ContentType.JSON,
        ]

        if (method == "put" || method == "post") {
            if (object instanceof String) {
                queryMap.body = object
            } else {
                queryMap.body = JsonOutput.toJson(object)
            }
        }

        client."${method}"(queryMap)
    }

    static TrustManager[] getAllPassTruestManagers() {
        [ new X509TrustManager() {
            void checkClientTrusted(X509Certificate[] chain, String authType) {
                // trust everything
            }

            void checkServerTrusted(X509Certificate[] chain, String authType) {
                // trust everything
            }

            X509Certificate[] getAcceptedIssuers() {
                return []
            }
        } ]
    }

    static def getKeyManagers(def certPath) {
        KeyStore keyStore = Utils.createKeyStore(certPath, SERVICE_KEY_PATH);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509")
        kmf.init(keyStore, "secret".toCharArray())
        return kmf.getKeyManagers()
    }
}
