package javaTest.joe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.google.gson.JsonParser;


public class AAA {

    static Logger logger = LoggerFactory.getLogger(AAA.class);

    public static void main(String[] args) throws ParseException {
        List<RememberDataSegment> list = new ArrayList<>();
        RememberDataSegment rememberDataSegment1 = new RememberDataSegment().withGuid("GG")
                .withAppId("AA");
        RememberDataSegment rememberDataSegment2 = new RememberDataSegment().withGuid("GG2")
                .withAppId("AA2");
        rememberDataSegment1.datum = new HashMap<>();
        rememberDataSegment1.datum.put("hash1", new JsonParser().parse("{\"aaa\":\"bbb\",\"ccc\":\"ddd\"}"));

        rememberDataSegment2.datum = new HashMap<>();
        rememberDataSegment2.datum.put("hash2", new JsonParser().parse("[{\"aaa\":\"bbb\"},{\"ccc\":\"ddd\"}]"));


        list.add(rememberDataSegment1);
        list.add(rememberDataSegment2);
        String s = new Gson().toJson(list);
        System.out.println(s);
    }

    public static class RememberDataSegment {
        public String rId;
        public String appId;
        public Map<String, JsonElement> datum = new HashMap<>();

        public RememberDataSegment withGuid(String rId) {
            this.rId = rId;
            return this;
        }

        public RememberDataSegment withAppId(String appId) {
            this.appId = appId;
            return this;
        }
    }
}
