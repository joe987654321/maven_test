package autowire.test.joe;

import java.lang.annotation.RetentionPolicy;

/**
 * Created by joe321 on 2016/12/12.
 */
@java.lang.annotation.Documented
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@javax.inject.Qualifier
public @interface Role {
    User user() default User.STUDENT;
    public enum User {STUDENT, TEACHER}
}
