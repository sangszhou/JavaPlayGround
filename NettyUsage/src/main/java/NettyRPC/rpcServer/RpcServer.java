package NettyRPC.rpcServer;

import NettyRPC.protocol.RpcDecoder;
import NettyRPC.protocol.RpcEncoder;
import NettyRPC.protocol.RpcRequest;
import NettyRPC.protocol.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    Map<String, Object> handlerMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // 自动分 buffer size
//                                    .addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 0))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcHandler(handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String host = "127.0.0.1";
            int port = 9999;

            ChannelFuture future = bootstrap.bind(host, port).sync();
            System.out.println("Rpc server started on port " + port);
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        serviceBeanMap.forEach((whatName, serviceInstance) -> {
            String interfaceName = serviceInstance.getClass().getAnnotation(RpcService.class).value().getName();
            handlerMap.put(interfaceName, serviceInstance);
        });
    }

}
