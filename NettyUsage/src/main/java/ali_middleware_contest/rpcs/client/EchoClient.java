package ali_middleware_contest.rpcs.client;

import NettyRPC.rpcClient.RpcClient;
import rpcSupport.client.RpcClientHandler;
import rpcSupport.protocol.RPCFuture;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.protocol.RpcResponse;
import rpcSupport.transport.ConnectionManager;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Handler;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class EchoClient {

    ConnectionManager manager;
    AtomicLong atomicLong = new AtomicLong(0);

    public EchoClient(ConnectionManager connectionManager) {
        this.manager = connectionManager;
    }

    public void sendEcho(String msg) throws Exception {
        RpcRequest request = new RpcRequest();

        Object[] parameters = new Object[1];
        parameters[0] = "abc";

        Class<?>[] parameterType = new Class[1];
        parameterType[0] = String.class;

        request.setRequestId(atomicLong.get());
        atomicLong.getAndAdd(1);
        request.setClassName("ali_middleware_contest.rpcs.server.EchoServer");
        request.setMethodName("echo");
        request.setParameterTypes(parameterType);
        request.setVersionNo(1);
        request.setParameters(parameters);

        //@fixme make manager return future instead of null/handler
        RpcClientHandler handler;
        while ((handler = manager.getHandler()) == null) {
            Thread.sleep(50);
        }

        RPCFuture rpcFuture = handler.sendRequest(request);
        RpcResponse rpcResponse = (RpcResponse) rpcFuture.get();

        System.out.println("echo response status is " + rpcResponse.getStatus());
        System.out.println("returned message is " + rpcResponse.getResult().toString());
    }

    public static void main(String args[]) {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connectServer(new InetSocketAddress("localhost", 9999));

        EchoClient echoClient = new EchoClient(connectionManager);

        try {
            echoClient.sendEcho("hello world!");
            echoClient.sendEcho("this is xinshzou from china!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
