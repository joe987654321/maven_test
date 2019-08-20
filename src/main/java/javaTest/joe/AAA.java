package javaTest.joe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import kinesis.RememberDataSegment;

public class AAA {

    static Logger logger = LoggerFactory.getLogger(AAA.class);

    public static void main(String[] args) throws ParseException {
//        List<RememberDataSegment> list = new ArrayList<>();
//        RememberDataSegment rememberDataSegment1 = new RememberDataSegment().withRid("GG")
//                .withAppId("AA");
//        RememberDataSegment rememberDataSegment2 = new RememberDataSegment().withRid("GG2")
//                .withAppId("AA2");
//
//        rememberDataSegment1.getDatum().put("hash1", new JsonParser().parse("{\"aaa\":\"bbb\",\"ccc\":\"ddd\"}"));
//
//
//        rememberDataSegment2.getDatum().put("hash2", new JsonParser().parse("[{\"aaa\":\"bbb\"},{\"ccc\":\"ddd\"}]"));
//
//
//        list.add(rememberDataSegment1);
//        list.add(rememberDataSegment2);
//        String s = new Gson().toJson(list);
//        System.out.println(s);

//        String content = "[{\"rId\":\"IE4KT7RYWGNOOJXMGSFPIWE3JM\",\"appId\":\"apac-member\",\"updatedTs\":1566196245,\"datum\":{\"firstJoin\":[{\"market\":\"tw\",\"fromProperty\":\"media-tw\",\"createdTs\":1566196245}],\"atos\":[{\"displayVersion\":\"1.1.1\",\"version\":1,\"agree\":true,\"market\":\"tw\",\"createdTs\":1566196245}],\"subscription\":[{\"market\":\"tw\",\"createdTs\":1566196473,\"status\":false}]}},{\"rId\":\"NKZGBGG6J5DAMTFTRMVF3PTPNQ\",\"appId\":\"apac-member\",\"updatedTs\":1566196199,\"datum\":{\"firstJoin\":[{\"market\":\"tw\",\"fromProperty\":\"media-tw\",\"createdTs\":1563699378}],\"atos\":[{\"displayVersion\":\"1.0\",\"version\":1,\"agree\":true,\"createdTs\":1539624120.0,\"market\":\"tw\"}],\"subscription\":[{\"market\":\"tw\",\"createdTs\":1566196199,\"status\":true}]}}]";
//        Gson gson = new Gson();
//        List<RememberDataSegment> list = gson.fromJson(content, new TypeToken<List<RememberDataSegment>>() {}.getType());
//        list.forEach(rds -> {
//            System.out.println(rds.getrId());
//            System.out.println(rds.getAppId());
//            System.out.println(rds.getUpdatedTs());
//            System.out.println(rds.getDatum());
//        });

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Calendar cal = Calendar.getInstance();
        long yesterday = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();
        cal.add(Calendar.DATE, 1);
        long today = cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).getEpochSecond();
        Date d = new Date(cal.getTime().toInstant().truncatedTo(ChronoUnit.DAYS).toEpochMilli());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String output = simpleDateFormat.format(cal.getTime());
        System.out.println(output);
        output = simpleDateFormat.format(d);
        System.out.println(output);

    }

//    public static class RememberDataSegment {
//        public String rId;
//        public String appId;
//        public Map<String, JsonElement> datum = new HashMap<>();
//
//        public RememberDataSegment withGuid(String rId) {
//            this.rId = rId;
//            return this;
//        }
//
//        public RememberDataSegment withAppId(String appId) {
//            this.appId = appId;
//            return this;
//        }
//    }
}
