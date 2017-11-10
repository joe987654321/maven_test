package cmr.xml;

import com.yahoo.parsec.clients.ParsecAsyncHttpClient;
import com.yahoo.parsec.clients.ParsecAsyncHttpClientFactory;
import com.yahoo.parsec.clients.ParsecAsyncHttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CmrParsing {


    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static String TEST_XML_STRING =
        "<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ParsecAsyncHttpClient client = new ParsecAsyncHttpClient.Builder().build();
        ParsecAsyncHttpRequest.Builder builder = new ParsecAsyncHttpRequest.Builder();
        ParsecAsyncHttpRequest request =
            builder.setUrl("http://api.cm.ops.yahoo.com/feed/xml?cmr_id=848832").setMethod("GET").build();

        String content = client.criticalExecute(request).get().getEntity().toString();

        tryParsingResponse(content);
//
//        request =
//            builder.setUrl("http://api.cm.ops.yahoo.com/feed/xml?cmr_id=84883200000").setMethod("GET").build();
//
//        content = client.criticalExecute(request).get().getEntity().toString();
//
//        tryParsingResponse(content);

        return;
    }

    private static void tryParsingResponse(String content) {
        try {
            JSONObject xmlJsonObject = XML.toJSONObject(content);
            JSONObject responseJSONObj = xmlJsonObject.optJSONObject("response");
            if (responseJSONObj == null) {
                System.out.println("response is not a json object");
                String jsonPrettyPrintString = xmlJsonObject.toString(PRETTY_PRINT_INDENT_FACTOR);
                System.out.println(jsonPrettyPrintString);
            } else {
                String status = responseJSONObj.getJSONObject("item").getString("status");
                System.out.println("status is "  + status);
            }
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
    }
}
