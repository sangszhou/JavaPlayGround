package rpcSupport.codec;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * Created by xinszhou on 24/01/2017.
 */
public class ApacheSerializationUtil {

    public static <T extends Serializable> byte[] serialize(T obj) {
        return SerializationUtils.serialize(obj);
    }

    public static <T extends Serializable> T deserialize(byte[] input) {
        return  SerializationUtils.deserialize(input);
    }

}
