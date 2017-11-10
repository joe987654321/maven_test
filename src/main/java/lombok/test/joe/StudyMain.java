package lombok.test.joe;

/**
 * Created by joe321 on 2016/12/9.
 */
public class StudyMain {
    public static void main(String[] args) {
        Student s = new Student();
        s.setId(123);

        System.out.println(s.getId());
    }
}
