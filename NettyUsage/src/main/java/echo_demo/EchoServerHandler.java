package echo_demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    Logger log = LoggerFactory.getLogger(getClass());

    public EchoServerHandler() {
        log.info("Echo server handler created");
    }

    @Override

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channel reading data...");
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info("channel read complete");
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        log.info("exception caught in echo server handler");
        ctx.close();
    }

}
