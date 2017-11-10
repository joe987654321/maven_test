package autowire.test.joe;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by joe321 on 2016/12/12.
 */
@Component
@Role(user = Role.User.TEACHER)
@Scope("singleton")
public class Teacher implements SchoolMember {
    public int id = 0;

    @Override
    public void echoRole() {
        System.out.println("I am teacher");
    }

    @Override
    public void echoId() {
        System.out.println("teacher id is "+ id);
    }
}
