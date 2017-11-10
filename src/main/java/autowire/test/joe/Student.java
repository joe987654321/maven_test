package autowire.test.joe;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by joe321 on 2016/12/12.
 */
@Component
@Role(user = Role.User.STUDENT)
@Scope("prototype")
public class Student implements SchoolMember {
    public int id = 0;

    @Override
    public void echoRole() {
        System.out.println("I am student");
    }

    @Override
    public void echoId() {
        System.out.println("student id is " + id);
    }
}
