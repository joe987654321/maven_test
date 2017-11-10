package autowire.test.joe;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Qualifier;
//import javax.inject.Qualifier;


/**
 * Created by joe321 on 2016/11/10.
 */
@Qualifier("this is pickchu")
@Component
public class Pickchu implements Animal {
    public void makeNoice() {
        System.out.println("pickchu");
    }
}
