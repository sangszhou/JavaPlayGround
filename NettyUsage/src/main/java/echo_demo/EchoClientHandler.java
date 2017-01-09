package echo_demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    Logger log = LoggerFactory.getLogger(getClass());

    private final ByteBuf firstMsg;

    public EchoClientHandler() {
        log.info("echo client handler created!");
        firstMsg = Unpooled.buffer(20);
        for(int i = 0; i < firstMsg.capacity(); i ++) {
            firstMsg.writeByte((byte)i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        log.info("channel activated");
        ctx.writeAndFlush(firstMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel read from system out");
        log.info("message received in handler: " + msg.toString());
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("exception caught in EchoClientHandler");
        cause.printStackTrace();
        ctx.close();
    }
}
