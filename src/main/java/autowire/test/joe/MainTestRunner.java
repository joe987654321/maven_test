package autowire.test.joe;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by joe321 on 2016/11/10.
 */
public class MainTestRunner {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);
       // ac.register(Zoo.class);

//        try {
//            Animal animal = ac.getBean(Animal.class);
//            animal.makeNoice();
//        } catch (BeansException e) {
//            e.printStackTrace();
//        }
//
//
//        Zoo zoo = ac.getBean(Zoo.class);
//        zoo.makeNoice();
//
//        System.out.println("----------------------");
       // zoo.makeAllNoice();


        System.out.println("----------------------");
        System.out.println("----------------------");
        School school = ac.getBean(School.class);

        System.out.println(school);
        school.makeNoice();

        System.out.println("---- set teacher and student id to 9527 ----");

        ((Student)(school.s)).id = 9527;
        ((Teacher)(school.t)).id = 9527;
        school.makeNoice();
        System.out.println(school.s);

        System.out.println("---- new school ----");
        school = ac.getBean(School.class);
        school.makeNoice();
        System.out.println(school);
        System.out.println(school.s);

        //Zoo zoo = new Zoo();
        //zoo.animal.makeNoice();
    }


}
