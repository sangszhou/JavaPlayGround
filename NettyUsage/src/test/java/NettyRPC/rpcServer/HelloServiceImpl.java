package NettyRPC.rpcServer;

import NettyRPC.protocol.Person;
import NettyRPC.protocol.HelloService;

/**
 * Created by xinszhou on 05/12/2016.
 */

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        System.out.println("Hello! " + name);
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        System.out.println("Hello! " + person.getFirstName() + " " + person.getLastName());
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }

}
