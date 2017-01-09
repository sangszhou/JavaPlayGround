package NettyRPC.rpcServer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class RpcBootstrap {
    public static void main(String args[]) {
        new ClassPathXmlApplicationContext("server-spring.xml");
    }
}
