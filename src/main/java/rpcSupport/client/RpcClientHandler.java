package rpcSupport.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpcSupport.protocol.RPCFuture;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.protocol.RpcResponse;
import sun.reflect.annotation.EnumConstantNotPresentExceptionProxy;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    ConcurrentMap<Long, RPCFuture> inFlightRequest = new ConcurrentHashMap<>();

    volatile private Channel channel;
    volatile private SocketAddress remoteServerAddress;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        remoteServerAddress = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        Long requestId = msg.getRequestId();
        System.out.println("response received in client for request id: " + requestId);
        RPCFuture future = inFlightRequest.get(requestId);
        if (future != null) {
            inFlightRequest.remove(requestId);
            future.complete(msg);
        }
    }

    public RPCFuture sendRequest(RpcRequest request) {
        if (inFlightRequest.containsKey(request.getRequestId())) {
            System.out.println("in flight request has already in the flight requests");
            return inFlightRequest.get(request.getRequestId());
        }
        System.out.println("send request with id: " + request.getRequestId());
        RPCFuture future = new RPCFuture(request);
        inFlightRequest.put(request.getRequestId(), future);
        channel.writeAndFlush(request);
        return future;
    }


    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        System.out.println("client encounter exception");
        throwable.printStackTrace();
        ctx.close();
    }

    public Channel getChannel() {
        return channel;
    }

}
