package NettyRPC.rpcClient;

import NettyRPC.rpcClient.proxy.IAsyncObjectProxy;
import NettyRPC.rpcClient.proxy.ObjectProxy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinszhou on 06/12/2016.
 */
public class RpcClient {

    public RpcClient() {
        String host = "127.0.0.1";
        int port = 9999;

        List<String> servers = new ArrayList<>();
        servers.add(host + ":" + port);
        ConnectionManager.getInstance().updateConnectedServers(servers);
    }

    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass));
    }

    public static <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass);
    }

}
