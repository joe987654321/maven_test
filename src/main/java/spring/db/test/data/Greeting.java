package spring.db.test.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Greeting {

    private final Host host;

    @Autowired
    public Greeting(Host host) {
        this.host = host;
    }


    public Host getHost() {
        return host;
    }
}