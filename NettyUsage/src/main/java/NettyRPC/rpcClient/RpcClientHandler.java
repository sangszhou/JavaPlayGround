package NettyRPC.rpcClient;

import NettyRPC.protocol.RpcRequest;
import NettyRPC.protocol.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by xinszhou on 05/12/2016.
 * 每个 handler 发出的消息都由此 handler 接收么 ???
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    ConcurrentHashMap<String, RPCFuture> pendingRPC = new ConcurrentHashMap<>();

    private Channel channel;
    private SocketAddress remoteServerAddr;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        remoteServerAddr = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        String requestId = msg.getRequestId();

        System.out.println("response received with id: " + msg.getRequestId());
        RPCFuture rpcFuture = pendingRPC.get(requestId);
        if (rpcFuture != null) {
            pendingRPC.remove(requestId);
            rpcFuture.complete(msg);
        }
    }

    public RPCFuture sendRequest(RpcRequest request) {
        if (pendingRPC.containsKey(request.getRequestId())) {
            System.out.println("pending rpc already has requestId request sent");
            return null;
        }

        System.out.println("sendRequest with requestId: " + request.getRequestId());
        RPCFuture rpcFuture = new RPCFuture(request);
        pendingRPC.put(request.getRequestId(), rpcFuture);
        channel.writeAndFlush(request);
        return rpcFuture;
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        System.out.println("Client caught exception");
        throwable.printStackTrace();
        ctx.close();
    }

    public Channel getChannel() {
        return channel;
    }
}
