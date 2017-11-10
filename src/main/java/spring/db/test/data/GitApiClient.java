package spring.db.test.data;

import org.springframework.stereotype.Component;

/**
 * Created by joe321 on 2017/4/27.
 */
@Component
public class GitApiClient {
    public boolean isEqualOrOlder(String lastSuccessSHA, String sha) {
        if (lastSuccessSHA == null) {
            return true;
        }
        return sha.length() >= lastSuccessSHA.length();
    }
}
