package classloader;

/**
 * Created by xinszhou on 20/01/2017.
 */
public class ForName {
    public static void main(String args[]) throws Exception {
        Class cls = Class.forName("classloader.DummyClass", false, ClassLoader.getSystemClassLoader());
    }
}
