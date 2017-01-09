package NettyRPC;

import NettyRPC.protocol.ProtoSerializationUtil;
import org.junit.Test;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class ProtoSerializationUtilTest {

    @Test
    public void test1(){
        Person xinszhou = new Person();
        xinszhou.setAge(25);
        xinszhou.setName("xinszhou");

        byte[] serialized = ProtoSerializationUtil.serialize(xinszhou);
        Person deserialized = ProtoSerializationUtil.deserialize(serialized, Person.class);

        System.out.println("Person age is "+ deserialized.getAge());
        System.out.println("Person name is " + deserialized.getName());
    }



}