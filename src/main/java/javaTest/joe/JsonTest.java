package javaTest.joe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {



    public static void main(String[] args) {
        List<String> list1 = null;

        List<String> list2 = new ArrayList<>();
        list2.add("HAHAHA");
        list2.add("HOHOHO");
        JSONObject json = new JSONObject()
            .put("id", "123")
            .putOpt("scopes1", list1)

            .put("scopes2", list2);

        System.out.println(json.toString());
    }
}
