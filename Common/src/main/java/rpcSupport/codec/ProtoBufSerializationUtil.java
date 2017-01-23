package rpcSupport.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ProtoBufSerializationUtil {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    public static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(cls);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtobufIOUtil.toByteArray(obj, schema, buffer);
    }

    //@fixme why not newInstance directly
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        T message = objenesis.newInstance(cls);
        Schema schema = getSchema(cls);
        ProtobufIOUtil.mergeFrom(data, message, schema);
        return message;
    }

    public <T> T deserialize2(byte[] data, Class<T> cls) {
        T message = objenesis.newInstance(cls);
        Schema schema = getSchema(cls);
        ProtobufIOUtil.mergeFrom(data, message, schema);
        return message;
    }

}
