package rpcSupport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.Serializable;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class RPCEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RPCEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)) {
//            byte[] data = ProtoBufSerializationUtil.serialize(msg);

            Serializable serializable = (Serializable) msg;

            byte[] data = ApacheSerializationUtil.serialize(serializable);

            out.writeInt(data.length);
            out.writeBytes(data);
        } else {
            System.out.println("error: incoming request is not instanceof generic request class" );
        }
    }
}
