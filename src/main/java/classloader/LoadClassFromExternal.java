package classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by xinszhou on 16/01/2017.
 * works for both networking and local jar file
 */
public class LoadClassFromExternal {
    static String jarPath = "http://central.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar";
  //static String jarPath = "jar:file:///ws/github/JavaPlayGround/src/main/resource/guava-21.0.jar!/";
    static String classPath = "com.google.common.cache.LongAddable";

    public Class findClassFromRemoteGuavaJar(String className) throws Exception {
        ClassLoader classLoader = new URLClassLoader(new URL[]{new URL(jarPath)});

        Class cl = Class.forName(className, true, classLoader);

        return cl;
    }

    public Class findClassFromJar(String jarPath, String className) throws Exception {
        ClassLoader classLoader = new URLClassLoader(new URL[]{new URL(jarPath)});
        Class cl = Class.forName(className, true, classLoader);
        return cl;
    }


    public static void main(String args[]) throws Exception {
        LoadClassFromExternal external = new LoadClassFromExternal();

        Class cl = external.findClassFromRemoteGuavaJar(classPath);

        System.out.println(cl.toString());
    }
}
