package NettyRPC.rpcClient.proxy;

import NettyRPC.rpcClient.RPCFuture;

/**
 * Created by xinszhou on 06/12/2016.
 */
public interface IAsyncObjectProxy {
    public RPCFuture call(String funcName, Object... args);
}
