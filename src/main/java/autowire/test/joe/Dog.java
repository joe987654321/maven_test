package autowire.test.joe;

import org.springframework.stereotype.Component;

import javax.inject.Named;

/**
 * Created by joe321 on 2016/11/10.
 */
@Named("a big dog")
public class Dog implements Animal {
    public void makeNoice() {
        System.out.println("bow bow");
    }

}
