package jsonparser.test.decodeEntity;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DecodeEntityTest {

    public static void main(String[] args) {
        DecodeEntityTest decodeEntityTest = new DecodeEntityTest();
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("1111222233334444");
        creditCard.setExpirationYear("2999");
        creditCard.setExpirationMonth("03");
        HolderInfo holderInfo = new HolderInfo();
        holderInfo.setNationalId("A123456789");
        holderInfo.setHolderName("&#24373;&#29020;&#24535;");
        creditCard.setHolderInfo(holderInfo);


        CreditCard creditCard1 = decodeEntityTest.decode(creditCard, CreditCard.class);

        System.out.println(creditCard1.getHolderInfo().getHolderName());

//        EinvoiceCarrier e = new EinvoiceCarrier();
//        e.setId(43288);
//        e.setMtime("1517899788980");
//        e.setUserId("y-groucho_test_id__b-bastet1.ec.tw1.yahoo.com__32126");
//
//        System.out.println(e.toString());
//        EinvoiceCarrier e1 = decodeEntityTest.decode2(e, EinvoiceCarrier.class);
//        System.out.println(e1.getCarrierId());
//        System.out.println(e1.getId());
//        System.out.println(e1.getMtime());
//        System.out.println(e1.getUserId());

        System.out.println(StringEscapeUtils.unescapeHtml4(null));

    }

    public <T> T decode(T obj, Class<T> tClass) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json =  gson.toJsonTree(obj, tClass).toString();


        System.out.println(json);
        String ret = StringEscapeUtils.unescapeHtml4(json);
//        ret = "{\"carrier_id\": \"271828183\", \"id\":43288,\"userId\":\"y-groucho_test_id__b-bastet1.ec.tw1.yahoo.com__32126\",\"mtime\":\"1517899788980\"}";
        System.out.println(ret);
        return gson.fromJson(ret, tClass);
    }



}
