package rpcSupport.codec;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinszhou on 24/01/2017.
 */
public class ApacheSerializationUtilTest {

    @Test
    public void studentSerialize() {
        Student xinszhou = new Student(26, "xinszhou", null);
        Student yucliu = new Student(27, "yucliu", null);
        Student yexia = new Student(28, "yexia", null);

        List<Student> xinszhouFriends = new ArrayList<>();
        List<Student> yucliuFriends = new ArrayList<>();

        xinszhouFriends.add(yucliu);
        yucliuFriends.add(yexia);

        xinszhou.setFriends(xinszhouFriends);
        yucliu.setFriends(yucliuFriends);

        byte[] xinszhou_bytes = ApacheSerializationUtil.serialize(xinszhou);
        Student xinszhou_deserialized = ApacheSerializationUtil.deserialize(xinszhou_bytes);
        xinszhou_deserialized.selfIntroduction();

    }


    static class Student implements Serializable {

        int age;
        String name;
        List<Student> friends;

        public Student(int age, String name, List<Student> friends) {
            this.age = age;
            this.name = name;
            this.friends = friends;
        }

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

        public List<Student> getFriends() {
            return friends;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFriends(List<Student> friends) {
            this.friends = friends;
        }

        public void selfIntroduction() {
            System.out.println("my name is : " + this.getName());
            System.out.println("my age is : " + this.getAge());

            if (friends != null) {
                friends.forEach(friend -> System.out.println("friend: " + friend.getName()));
            } else {
                System.out.println("has zero friend");
            }

        }

    }

}