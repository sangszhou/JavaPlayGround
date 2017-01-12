package NettyRPC.rpcClient;

import NettyRPC.protocol.HelloService;
import NettyRPC.rpcClient.proxy.IAsyncObjectProxy;
import NettyRPC.rpcClient.proxy.ObjectProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by xinszhou on 06/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class) // to enable autowire work
@ContextConfiguration(locations = "classpath:client-spring.xml")
public class HelloServiceTest {

    @Autowired
    private RpcClient rpcClient; // to add server address for client

    @Test
    public void helloTest1() throws InterruptedException {

        Thread.sleep(300);

        HelloService helloService = RpcClient.create(HelloService.class);
        String result = helloService.hello("xinszhou");
        System.out.println(result);
    }

    @Test
    public void helloTest2() throws Exception {

        Thread.sleep(300);

        IAsyncObjectProxy proxy = RpcClient.createAsync(HelloService.class);
        RPCFuture result = proxy.call("hello", "xinszhou");
        System.out.println(result.get().toString());
    }

}
