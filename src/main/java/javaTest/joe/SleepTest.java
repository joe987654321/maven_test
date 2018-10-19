package javaTest.joe;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;


public class SleepTest {

    public static void main(String[] args) throws Exception {

//        String url = args[0];
        int timeout = 300000;
        //String url = "http://b-meregs11.ec.tw1.yahoo.com:4080/a.php";
//        String url = "http://b-meregs11.ec.tw1.yahoo.com:4080/sleep.php";
        String url = "http://b.merchandise.nevec.yahoo.com:4080/sleep.php";
//        if (args.length >=2) {
//            timeout = Integer.parseInt(args[1]);
//        }

        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
                .setReadTimeout(timeout)
                .setConnectTimeout(30000)
                .setRequestTimeout(timeout)
                .setAllowPoolingConnections(true)
                .setAllowPoolingSslConnections(true)
                .setConnectionTTL(30000)
                .setFollowRedirect(true)
                .setAcceptAnyCertificate(true)
                .build();
        AsyncHttpClient client = new AsyncHttpClient(config);

        ListenableFuture<Void> voidListenableFuture = client.executeRequest(prepareRequestForTest(url), getPrintResponseHandlerForTest());
        voidListenableFuture.get();
        client.close();

    }

    private static AsyncCompletionHandler<Void> getPrintResponseHandlerForTest() {
        return new AsyncCompletionHandler<Void>() {

            @Override
            public STATE onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
                printMsg("onBodyPartReceived");
                return super.onBodyPartReceived(content);
            }

            @Override
            public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
                printMsg("onStatusReceived");
                return super.onStatusReceived(status);
            }

            @Override
            public STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
                printMsg("onHeadersReceived");
                return super.onHeadersReceived(headers);
            }

            @Override
            public STATE onHeaderWriteCompleted() {
                printMsg("onHeaderWriteCompleted");
                return super.onHeaderWriteCompleted();
            }

            @Override
            public STATE onContentWriteCompleted() {
                printMsg("onContentWriteCompleted");
                return super.onContentWriteCompleted();
            }

            @Override
            public STATE onContentWriteProgress(long amount, long current, long total) {
                printMsg("onContentWriteProgress");
                return super.onContentWriteProgress(amount, current, total);
            }

            @Override
            public void onThrowable(Throwable t) {

                printMsg(" onThrowable:" + t.getMessage());
                super.onThrowable(t);
            }

            @Override
            public Void onCompleted(Response response) {
                try {
                    printMsg(response.getResponseBody());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private static void printMsg(String appendedMsg) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatDateTime = now.format(formatter);

        System.out.println(formatDateTime + ":" + appendedMsg);
    }

    private static Request prepareRequestForTest(String url) {
        RequestBuilder requestBuilder = new RequestBuilder()
                .setNameResolver(new RoundRobinNameResolver())
                .setUrl(url)
                .setMethod("GET");

        return requestBuilder.build();
    }
}