package autowire.test.joe;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by joe321 on 2016/12/12.
 */
@Component
@Scope("prototype")
public class School {

    @Inject
    @Role(user = Role.User.STUDENT)
    public SchoolMember s;

    @Inject
    @Role(user = Role.User.TEACHER)
    public SchoolMember t;

    public void makeNoice() {
        s.echoRole();
        t.echoRole();

        s.echoId();
        t.echoId();
    }

    public SchoolMember getS() {
        return s;
    }
}
