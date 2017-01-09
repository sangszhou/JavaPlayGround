package NettyRPC.protocol;

/**
 * Created by xinszhou on 05/12/2016.
 */
public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
