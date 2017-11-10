package spring.db.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


/**
 * ref: https://spring.io/guides/gs/actuator-service/
 */
@SpringBootApplication
public class SpringMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringMain.class, args);
    }

}
