package classloaders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class JarFileLoader extends URLClassLoader {

    public JarFileLoader(URL[] urls) {
        super(urls);
    }

    public static List<URL> getJarFilesList(File dir) throws IOException {
        List<URL> list = new ArrayList<URL>();
        URL url;
        for (File file : dir.listFiles()) {
            if (!file.isDirectory() && (file.getName().endsWith(".jar"))) {
                url = new URL("jar:file://" + dir.getAbsolutePath() + "/" + file.getName() + "!/");
                list.add(url);
            }
        }
        return list;
    }


    @Override
    public Class loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }



    public static JarFileLoader loadJars(File dir) throws Exception {
        List<URL> list = getJarFilesList(dir);
        URL[] url = list.toArray(new URL[list.size()]);
        JarFileLoader loader = new JarFileLoader(url);
        return loader;
    }
}

