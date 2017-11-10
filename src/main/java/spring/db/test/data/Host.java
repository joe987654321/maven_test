package spring.db.test.data;

import org.springframework.stereotype.Component;

/**
 * Created by joe321 on 2017/4/21.
 */
@Component
public class Host {
    private String name = "host";

    Host() {
        this.name = "default constructor";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
