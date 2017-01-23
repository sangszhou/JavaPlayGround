package rpcSupport.codec;

import classloaders.JarClassLoader;
import classloaders.JarFileLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Class.forName;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class RPCDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    private JarClassLoader classLoader;

    public RPCDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
        try {
            classLoader = new JarClassLoader("/ws/github/JavaPlayGround/ZookeeperPlayground/src/main/java/disJobFramework/jarsStorage/schedulerJars/ZookeeperPlayground-1.0-SNAPSHOT-jar-with-dependencies.jar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int dataLength = in.readInt();

        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

//        Class protoUtil = Class.forName("rpcSupport.codec.ProtoBufSerializationUtil", true, classLoader);

        ArrayList<String> ar = new ArrayList<>();
        ar.size();

//        Class cplxTs = classLoader.loadClass("disJobFramework/cluster/taskes/ComplexTask");

        Thread.currentThread().setContextClassLoader(classLoader);

        Class protoUtil = classLoader.loadClass("rpcSupport/codec/ProtoBufSerializationUtil");

        Object seri = protoUtil.newInstance();

        Method[] ms = protoUtil.getMethods();

        Method deserialize = protoUtil.getMethod("deserialize2", byte[].class, Class.class);

        Object obj = deserialize.invoke(seri, data, genericClass);

//        Object obj = seri.deserialize(data, genericClass);

        out.add(obj);

        System.out.println("RPC decoder decode success");

    }
}
