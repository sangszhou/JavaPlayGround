package rpcSupport.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import rpcSupport.codec.RPCDecoder;
import rpcSupport.codec.RPCEncoder;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.protocol.RpcResponse;
import rpcSupport.server.ServerRpcHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class RPCServer {

    Map<String, Object> serviceBean = new HashMap<>();

    public void registerBean(String fullClassName, Object object) {
        serviceBean.put(fullClassName, object);
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RPCEncoder(RpcResponse.class))
                                    .addLast(new RPCDecoder(RpcRequest.class))
                                    .addLast(new ServerRpcHandler(serviceBean));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind("localhost", 9999).sync();
            System.out.println("rpc server is started");
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
