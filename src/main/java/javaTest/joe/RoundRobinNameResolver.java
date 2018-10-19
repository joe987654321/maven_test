package javaTest.joe;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import com.ning.http.client.NameResolver;

/**
 * A round-robin name resolver.
 *
 * The results depend on JDK DNS cache.
 *
 * java.security.Security.setProperty("networkaddress.cache.ttl" , "10");
 */
public class RoundRobinNameResolver implements NameResolver {
    /**
     * Index of the next entry.
     * It is OK to change the size of getAllByName().
     * It is OK to overflow.
     *
     * resolve() will only be invoked when there is a new connection.
     * If keep-alive is enabled, don't forget to setConnectionTTL().
     */
    private AtomicInteger i = new AtomicInteger(0);

    @Override
    public InetAddress resolve(String name) throws UnknownHostException {
        InetAddress[] addrs = doResolve(name); // unknown host exception
        int tmp = Math.abs(i.getAndIncrement() % addrs.length);
        return addrs[tmp];
    }

    /**
     * Resolve the name.
     * This methods is meant to be overridden by test cases.
     *
     * @param name the name to be resolved
     * @return resolved address
     * @throws UnknownHostException bad name
     */
    protected InetAddress[] doResolve(String name) throws UnknownHostException {
        return InetAddress.getAllByName(name);
    }
}
