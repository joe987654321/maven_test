package jsonparser.test.joe;

import java.io.FileReader;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParserTester {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
//
//            Object obj = parser.parse(new FileReader(
//                    "/Users/joe321/a.json"));
//
//            JSONObject jsonObject = (JSONObject) obj;
//
//            JSONArray resources = (JSONArray) jsonObject.get("resources");
//            Iterator<JSONObject> iterator = resources.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }

            JSONObject testObject = (JSONObject) parser.parse("{\"type\":\"evil\"}");

            ObjectMapper objectMapper = new ObjectMapper();
            Resource r = objectMapper.readValue(testObject.toJSONString(), Resource.class);

            System.out.println(r.type);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}