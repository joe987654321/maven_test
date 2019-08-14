package javaTest.joe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AAA {

    static Logger logger = LoggerFactory.getLogger(AAA.class);

    public static void main(String[] args) throws ParseException {
//        String dateText  = "2019-08-08T09:25:17.000Z";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//        Long timeInText = dateFormat.parse(dateText).getTime();
//        System.out.println(timeInText);
        System.out.println(logger.getClass().getName());
         logger.error("joe321");
    }
}
