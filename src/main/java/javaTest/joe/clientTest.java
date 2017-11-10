//package javaTest.joe;
//
//import com.example.parsec_generated.SampleClient;
//import com.example.parsec_generated.SampleClientImpl;
//import com.example.parsec_generated.User;
//
//import javax.xml.ws.http.HTTPException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//
///**
// * Created by joe321 on 2017/2/13.
// */
//public class clientTest {
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Map<String, List<String>> headers = new HashMap<String, List<String>>();
//        List<String> list = new ArrayList<String>();
//        list.add("application/json");
//        headers.put("Content-Type", list);
//        SampleClient s = new SampleClientImpl("http://localhost:8080/api/sample/v1", null);
//
//        CompletableFuture<User> future = s.getUser(headers, 1);
//        User user = null;
//        try {
//            user = future.get();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            if (e.getCause() instanceof  HTTPException) {
//                HTTPException ee = (HTTPException) e.getCause();
//
//                System.out.println(ee.getMessage() + " ##");
//                System.out.println(ee.getStatusCode() + " ##");
//
////                e.printStackTrace();
//            }
//        }
//     //   System.out.println("Name: " + user.getName() + ", id: " + user.getId());
//    }
//}
