package ali_middleware_contest.rpcs.server;

import rpcSupport.server.RPCServer;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ServiceServer {
    public static void main(String args[]) {
        EchoServer echoServer = new EchoServer();

        //port is 9999
        RPCServer rpcServer = new RPCServer();
        rpcServer.registerBean(echoServer.getClass().getName(),
                echoServer);
        rpcServer.start();
    }
}
