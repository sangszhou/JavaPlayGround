package remote.netty.fileserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class HttpStaticFileServerInitializer extends ChannelInitializer<SocketChannel> {

    String baseDir;

    public HttpStaticFileServerInitializer(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        CorsConfig corsConfig = CorsConfig.withAnyOrigin().build();

        ChannelPipeline pipeline = ch.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(83886080)); // 80MB
        //pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

        pipeline.addLast("cors", new CorsHandler(corsConfig));

        pipeline.addLast("handler", new HttpStaticFileServerHandler(baseDir));

    }
}
