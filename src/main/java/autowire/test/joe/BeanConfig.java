package autowire.test.joe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import com.google.inject.AbstractModule;
//import com.google.inject.Guice;
//import com.google.inject.Inject;

/**
 * Created by joe321 on 2016/11/10.
 */
@ComponentScan("autowire.test.joe")
@Configuration
public class BeanConfig {

//    @Bean
//    public Animal getDog() {
//        return new Dog();
//    }

//    @Bean
//    public Animal getCat() {
//        return new Cat();
//    }

    @Bean
    public Animal getBird() {
        return new Bird();
    }
}
