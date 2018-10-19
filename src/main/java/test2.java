

public class test2 {

    public static void main(String[] args) {
        System.out.println("hello");
        //String s = "/api/credit-card/v1/creditCards";
        String s = "a";
        System.out.println(s.replaceAll(".*", "b"));

        s = "a";
        System.out.println(s.replaceAll(".*$", "b"));

        s = "a";
        System.out.println(s.replaceAll("^.*", "b"));

        s = "a";
        System.out.println(s.replaceAll("^.*$", "b"));
    }


}
