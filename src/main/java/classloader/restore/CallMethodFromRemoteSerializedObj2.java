package classloader.restore;

import org.springframework.core.serializer.DefaultDeserializer;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by xinszhou on 15/03/2017.
 */
public class CallMethodFromRemoteSerializedObj2 {

    public static void step3() throws Exception {

        String jarPath = "http://central.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar";
        ClassLoader guavaCl = new URLClassLoader(new URL[]{new URL(jarPath)});

        DefaultDeserializer defaultDeserializer = new DefaultDeserializer(guavaCl);

        Object deserialized = defaultDeserializer.deserialize(new FileInputStream(CallMethodFromSerializedObj.filePath));

        System.out.println(deserialized.getClass().getName());

        Class hostClz = Class.forName("com.google.common.net.HostAndPort", true, guavaCl);
        Method getHost = hostClz.getMethod("getHostText");

        System.out.println(getHost.invoke(deserialized));

    }

    public static void main(String args[]) throws Exception {
        CallMethodFromRemoteSerializedObj2.step3();
    }
}
