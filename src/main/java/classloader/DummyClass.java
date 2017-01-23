package classloader;

/**
 * Created by xinszhou on 20/01/2017.
 */
public class DummyClass {
    static {
        System.out.println("Dummy class is inited");
        int n = 1 % "".length();
    }
}
