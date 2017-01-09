package NettyRPC.rpcServer;

import NettyRPC.protocol.HelloService;
import NettyRPC.protocol.RpcDecoder;
import NettyRPC.protocol.RpcEncoder;
import NettyRPC.protocol.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class SmallestClientForServerTesting {

    public static void main(String args[]) {
        EventLoopGroup group = new NioEventLoopGroup(1);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // @todo

                            pipeline.addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new HelloMessageSender());
                        }
                    });

            String host = "127.0.0.1";
            int port = 9999;

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    static class HelloMessageSender extends ChannelInboundHandlerAdapter {

        public HelloMessageSender() {

        }

        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("channel Activated, sending message to rpc server...");

            RpcRequest request = new RpcRequest();

            request.setRequestId("test:1");
            request.setClassName("NettyRPC.protocol.HelloService");
            request.setMethodName("hello");

            Class<?>[] argTypes = new Class<?>[1];
            argTypes[0] = String.class;

            Object[] args = new Object[1];
            args[0] = "xinszhou";

            request.setParameterTypes(argTypes);
            request.setParameters(args);

            ctx.writeAndFlush(request);
        }

    }
}
