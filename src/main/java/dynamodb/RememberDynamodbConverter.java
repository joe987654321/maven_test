package dynamodb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RememberDynamodbConverter implements DynamoDBTypeConverter<Map<String, String>, JsonObject> {

    @Override
    public Map convert(JsonObject object) {
        Map<String, String> m = new HashMap();
        for (String s: object.keySet()) {
            m.put(s, object.get(s).toString());
        }
        return m;
    }

    @Override
    public JsonObject unconvert(Map<String, String> object) {
        JsonObject p = new JsonObject();
        for (String s: object.keySet()) {
            p.add(s, new JsonParser().parse(object.get(s)));
        }
        return p;
    }
}
