package autowire.test.joe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.inject.Qualifier;
import java.util.List;


@Component
/**
 * Created by joe321 on 2016/11/10.
 */
public class Zoo {
    @Inject
    @Named("a big dog")
    private Animal aaaaa;

    @Inject
    @Named("bigcat")
    private Animal animal2;

    @Qualifier("this is pickchu")
    @Autowired
    private Animal animal3;

    @Inject
    private List<Animal> animals;
//
//    public void whoIsHowling (Animal a)
//    {
//
//    }

    public void makeNoice() {
        aaaaa.makeNoice();
        animal2.makeNoice();
        animal3.makeNoice();
    }

//    public void makeAllNoice() {
//        for (Animal a: animals) {
//            a.makeNoice();
//        }
//    }
}
