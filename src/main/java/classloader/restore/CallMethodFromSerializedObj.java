package classloader.restore;

import com.google.common.net.HostAndPort;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.DefaultSerializer;
import util.SystemInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by xinszhou on 15/03/2017.
 */
public class CallMethodFromSerializedObj {
    static String filePath = SystemInfo.getProjectPath() + "/" + "hostAndPort.serialized";

    // create and serialize object
    public static void step1() throws Exception {
        DefaultSerializer defaultSerializer = new DefaultSerializer();
        DefaultDeserializer defaultDeserializer = new DefaultDeserializer();

        HostAndPort hp = HostAndPort.fromString("localhost:80");
        defaultSerializer.serialize(hp, new FileOutputStream(filePath));

//        FileUtils.writeByteArrayToFile(new File(filePath), result);

//        byte[] result_cp = FileUtils.readFileToByteArray(new File(filePath));

        Object deserized = defaultDeserializer.deserialize(new FileInputStream(filePath));

//        HostAndPort hp_cp = SerializationUtils.deserialize(result_cp);

        HostAndPort hp_cp = (HostAndPort) deserized;

        System.out.println(hp_cp.getHostText());
    }

    // restore from binary file
    // before executing this method, remove guava dependency from project
    // it will throw exception, because there's no HostAndPort in this system
    public static void step2() throws Exception {

        byte[] result_cp = FileUtils.readFileToByteArray(new File(filePath));
        Object hp_cp = SerializationUtils.deserialize(result_cp);
    }

    // restore hostAndPort from classloader contains guava class file
    public static void step3() throws Exception {
        String jarPath = "http://central.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar";
        ClassLoader guavaCl = new URLClassLoader(new URL[]{new URL(jarPath)});

        Class hostClz_verify = Class.forName("com.google.common.net.HostAndPort", true, guavaCl);
        System.out.println(hostClz_verify.getName());

        // create SerializationUtils, make urlCl its classloader
        Class serializeClz = Class.forName("org.apache.commons.lang3.SerializationUtils", true, guavaCl);

        System.out.println("class loader of SerializationUtils is " + serializeClz.getClassLoader().toString());

        Method deserializeMtd = serializeClz.getMethod("deserialize", byte[].class);

        byte[] result_cp = FileUtils.readFileToByteArray(new File(filePath));

        // it should be able to find the
        Object hostAndPort = deserializeMtd.invoke(null, result_cp);


        Class hostClz = Class.forName("com.google.common.net.HostAndPort", true, guavaCl);
        Method getHost = hostClz.getMethod("getHostText");

        getHost.invoke(hostAndPort);

    }


    public static void main(String args[]) throws Exception {
//        CallMethodFromSerializedObj.step1();
//        step1();
    }
}
