package remote.netty.fileserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Created by xinszhou on 16/01/2017.
 */
public class HttpStaticFileServer {

    private final int port;
    String baseDir;

    public HttpStaticFileServer(String dir, int port) {
        this.baseDir = dir;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpStaticFileServerInitializer(baseDir));

            Channel ch = b.bind(port).sync().channel();
            System.out.println("File server started at port " + port
                    + '.');
            ch.closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8111;
        }

        //@todo
        new HttpStaticFileServer("", port).run();
    }
}
