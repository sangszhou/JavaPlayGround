package classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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

    public static JarFileLoader loadJars(File dir) throws Exception {
       /* String path="/home/gangaraju/jar/";
        File dir=new File(path);*/
        List<URL> list = getJarFilesList(dir);
        URL[] url = list.toArray(new URL[list.size()]);
        JarFileLoader loader = new JarFileLoader(url);
        return loader;
    }
}

