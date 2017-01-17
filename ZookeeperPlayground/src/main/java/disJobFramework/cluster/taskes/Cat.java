package disJobFramework.cluster.taskes;

import java.io.Serializable;

/**
 * Created by xinszhou on 17/01/2017.
 */
public class Cat implements Serializable {

    int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void play() {
        System.out.println("cat is playing");
    }
}
