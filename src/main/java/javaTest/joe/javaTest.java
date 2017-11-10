package javaTest.joe;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.yahoo.parsec.clients.ParsecAsyncHttpClient;
import com.yahoo.parsec.clients.ParsecAsyncHttpRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.http.PathMap;

public class javaTest {

    private static class ID {
        public int id;
        public String value;
        ID (int i, String v) {
            id = i;
            value = v;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ID)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            ID input = (ID)obj;

            if (input.id == this.id && input.value.equals(this.value)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return 13 * id + value.length();
        }

    }

    private static enum Nested {
        AAA,
        BBB,
        NULL,
    }


    private static class Data {
        private int id;
        private String desc;
        private Nested nested;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public Nested getNested() {
            return nested;
        }
        public void setNested(Nested nested) {
            this.nested = nested;
        }


    }

    public static class BigData<E extends Enum<E>> {

        E smallData;

        BigData(E input) {
            smallData = input;
        }

        public void printTemplateClassName() {
            if (smallData.getClass() == Nested.class) {
                System.out.println("They are the same!!");
            }

            System.out.println(smallData.getClass().toString());
        }
    }

    /**
     * @param args
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, ClassNotFoundException {

/*
        Data data = new Data();
        data.setId(1);
        data.setDesc("description");
        data.setNested(Nested.AAA);
    //    data.nested.value = "in hello world";

        data.getClass().getFields();
        for(PropertyDescriptor propertyDescriptor :
            Introspector.getBeanInfo(data.getClass(), Object.class).getPropertyDescriptors()){

            // propertyEditor.getReadMethod() exposes the getter
            // btw, this may be null if you have a write-only property
            System.out.println(propertyDescriptor.getReadMethod());

            Method m = propertyDescriptor.getReadMethod();
            Object obj = m.invoke(data,  new Object[] {} );
            System.out.println(obj.toString());
        }
*/
        /*
        for (Field f: data.getClass().getFields()) {
            f.setAccessible(true);
       //     System.out.println(f.getName() + "AA");
            if (f.get(data) == null) {
                System.out.println(f.getName() + " is null");
            }
            System.out.println(f.get(data));
        }
*/
/*
        Nested input = Nested.AAA;
        BigData<Nested> bigdata = new BigData<Nested>(input);
        bigdata.printTemplateClassName();
        */

//        try {
//            JSONObject responseData = new JSONObject(
//                    "{\"aaa\" : FALSE}"
//                );
//            boolean x = responseData.getBoolean("aaa");
//            System.out.println(x);
//            System.out.println(responseData);
//        } catch (JSONException e) {
//            // TODO 自動產生的 catch 區塊
//            e.printStackTrace();
//        }

/*
        Config c = ConfigFactory.parseString("title = [ {name=t1, n1=123, n2=999} , {name=t2, n1=456, n2=888}, {name=s1}, {name=s2} ]");

        for (Config c2: c.getConfigList("title")) {
            if (c2.getString("name").startsWith("t")) {
                System.out.println(c2.getString("n1"));
            }
        }
*/
        /*
        for (Config c2: c.getConfigList("title")) {
            if (c2.getString("name").startsWith("t")) {
                System.out.println(c2.getString("n1"));
            }
        }
        */
/*
        Config c = ConfigFactory.parseString("title { t1 { n1=123, n2=999} ,  t2 { n1=456, n2=888}, s1 {}, s2 {} }");

        ConfigObject c2 = c.getObject("title");
        for (String a : c2.keySet()){
            System.out.println(a);
        }

        System.out.println(c2);
        System.out.println(c2.toConfig());
        System.out.println(c2.toConfig().root());
*/
/*
        HashMap<String, Object> body = new HashMap<>();
        body.put("startTs", "sss");
        body.put("endTs", "eee");
        body.put("start", "111");
        body.put("count", "CCC");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();
        try {
            String jsonBody = objectWriter.writeValueAsString(body);
            System.out.println(jsonBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String jsonInString = objectMapper.writeValueAsString(new ID(1,"AAA"));
        System.out.println(jsonInString);

*/
//        String a = null;
//        try {
//            a.getBytes();
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getCause());
//        }
        //   String jsonBody = objectWriter.writeValueAsString(body);
//        Class<String> tClass = String.class;
//        String input = "ABCDE ";
//        if (java.io.Serializable.class.isAssignableFrom(tClass)) {
//            System.out.println("hello");
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//
//            oos.writeObject(input);
//
//            oos.flush();
//            oos.close();
//
//            byte [] b = baos.toByteArray();
//            byte [] s = input.getBytes();
//
//            System.out.println(Arrays.toString(b));
//            System.out.println(Arrays.toString(s));
//
//            InputStream is = new ByteArrayInputStream(baos.toByteArray());
//            ObjectInputStream ois = new ObjectInputStream(is);
//            System.out.println(tClass.cast(ois.readObject()));
//
//        }

        //System.out.println(Long.parseLong("000123"));

//        Set<ID> set= new HashSet<>();
//        Set<ID> set= new TreeSet<>();
//        Set<ID> set= new LinkedHashSet<>();
//        ID id1 = new ID(1, "one");
//        ID id2 = new ID(2, "two");
//        ID id1Same = new ID(1, "one");
//
////        System.out.println(id1.equals(id2));
////        System.out.println(id1.equals(id1Same));
//
//        set.add(id1);
//        System.out.println(set.contains(id1));
//        System.out.println(set.contains(id2));
//
//        for (Iterator<ID> it = set.iterator(); it.hasNext(); ) {
//            ID element = it.next();
//            System.out.println("id: " + element.id + " : " + element.value);
//        }
//
//        id1.id = 2;
//        id1.value = "two";
//
//        System.out.println(set.contains(id1));
//        System.out.println(set.contains(id2));
//
//        for (Iterator<ID> it = set.iterator(); it.hasNext(); ) {
//            ID element = it.next();
//            System.out.println("id: " + element.id + " : " + element.value);
//        }

      //  System.out.println(set.contains(id1));

//        try {
//            boolean flag = false;
//            try {
//
//            } catch (Exception e) {
//                flag = true;
//                throw e;
//            } finally {
//                System.out.println(flag);
//            }
//        } catch (RuntimeException e) {
//            System.out.println("RuntimeException" + e.getMessage() + e.getClass().toString());
//        } catch (Exception e) {
//            System.out.println("Exception");
//        }


//        ZonedDateTime now1 = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
//        ZonedDateTime now2 = ZonedDateTime.now(ZoneOffset.UTC);
//        ZonedDateTime now3 = ZonedDateTime.now();
//        ZonedDateTime t1 = ZonedDateTime.of(1970, 1, 2, 0, 0, 1, 0, ZoneOffset.UTC);
//        ZonedDateTime t2 = ZonedDateTime.ofInstant(Instant.parse("2000-01-01T00:00:00.123Z"), ZoneOffset.UTC);
//        ZonedDateTime t3 = ZonedDateTime.parse("2000-01-01T00:00:00.124Z");
        //t3 = null;
//        ZonedDateTime t4 = ZonedDateTime.parse("2010-01-01T00:00:00.123Z");
//
//        System.out.println(now1);
//        System.out.println(now2);
//        System.out.println(now3);
//        System.out.println(t1);
//        System.out.println(t2);
//        System.out.println(t3);
  //      System.out.println(olderDateTime(t3, t2));
//        System.out.println(t4);


//        List<String> a = getStringList(null);
//        System.out.println(a.getClass());
//        System.out.println(a);
//        List<String> b = getStringList("[]");
//        System.out.println(b.getClass());
//        System.out.println(b);

//        List<String> list = new ArrayList<>();
//        list.add("hello");
//        list.add(null);
//        list.add("world");
//        System.out.println(list);
//
//        Map<String, String> map = new HashMap<>();
//        for (String s: list) {
//            map.put(s, s);
//        }
//        map.put(null, "helloworld");
//        System.out.println(map);

//        Path path = Paths.get("/abc/cat/index.html");
//        System.out.println(path.getFileName());
//        System.out.println(path.getParent());
//        System.out.println(path.getRoot());

//        ParsecAsyncHttpRequest.Builder pahrb = new ParsecAsyncHttpRequest.Builder();
//        pahrb.addRetryStatusCode(408);
//        ParsecAsyncHttpRequest pahr = pahrb.build();
//
//        AsyncCompletionHandler<String> asyncCompletionHandler = new AsyncCompletionHandler<String>() {
//
//            @Override
//            public String onCompleted(Response response) throws Exception {
//                throw new Exception("My exception");
//            }
//        };
//
//        ParsecAsyncHttpClient client = null;
//        try {
//            client = new ParsecAsyncHttpClient.Builder().setAcceptAnyCertificate(true).build();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Future future = client.criticalExecute(pahr, asyncCompletionHandler);
//            future.get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String s = String.format("Unable to create project with the given repo. [%4s]. %s", "aaa", "bbb");
//        System.out.println(s);
//
//        System.out.println(PathMap.match("/apex/v1/projects/", "/apex/v1/projects/", true));
//        System.out.println("by_apex_aaa".replace('_','-'));
//        System.out.println(System.getProperty("os.name"));
//
//        System.out.println(Long.parseLong("1502697415974"));
//        System.out.println(Long.parseLong("1501642962686"));
//        System.out.println(Long.parseLong(null));
//        School school = new School();
//        Gson gson = new Gson();
//        System.out.println(ReflectionToStringBuilder.toString(school, ToStringStyle.NO_CLASS_NAME_STYLE));
//        System.out.println(gson.toJson(school));
//
//
    }


    public static class School {
        private List<String> students;
        private List<String> teachers;

        public School () {
            students = Arrays.asList();
            teachers = Arrays.asList("t1", "t2");
        }

        public List<String> getStudents() {
            return students;
        }

        public void setStudents(List<String> students) {
            this.students = students;
        }

        public List<String> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<String> teachers) {
            this.teachers = teachers;
        }
    }

    public static enum Animal {
        DOG(0),
        CAT(1);

        Animal(int i) {

        }
    }

//    static private List<String> getStringList(String string) {
//        if (string == null) {
//            return new ArrayList<>();
//        }
//        Gson gson = new Gson();
//        return gson.fromJson(string, List.class);
//    }

}
