package classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class LoadClassFromExternal {
    //    static String jarPath = "http://central.maven.org/maven2/com/google/guava/guava/21.0/guava-21.0.jar";

    static String jarPath = "jar:file:///ws/github/JavaPlayGround/src/main/resource/guava-21.0.jar!/";
    static String classPath = "com.google.common.cache.LongAddable";

    public Class findClassInJar(String className) throws Exception {
        ClassLoader classLoader = new URLClassLoader(new URL[]{new URL(jarPath)});

        Class cl = Class.forName(classPath, true, classLoader);

        return cl;
    }

    public static void main(String args[]) throws Exception {
        LoadClassFromExternal external = new LoadClassFromExternal();

        Class cl = external.findClassInJar(jarPath);

        System.out.println(cl.getClass().getName());
    }
}
