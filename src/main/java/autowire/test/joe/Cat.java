package autowire.test.joe;

import javax.inject.Named;

/**
 * Created by joe321 on 2016/11/10.
 */
@Named("bigcat")
public class Cat implements Animal {
    public void makeNoice() {
        System.out.println("meow meow");
    }
}
