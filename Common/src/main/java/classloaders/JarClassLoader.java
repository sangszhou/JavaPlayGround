package classloaders;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xinszhou on 1/17/17.
 */
public class JarClassLoader extends ClassLoader {
    private String jarFile = "jar/test.jar"; //Path to the jar file
    private Hashtable classes = new Hashtable(); //used to cache already defined classes


    public JarClassLoader(String jarFile) {
        super(JarClassLoader.class.getClassLoader()); //calls the parent class loader's constructor
        this.jarFile = jarFile;
    }

    @Override
    public Class loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class findClass(String className) {
        byte classByte[];
        Class result = null;

        result = (Class) classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        if (className.endsWith("ComplexTask")) {
            System.out.println("ComplexTask");
        }

        if(className.endsWith("ProtoBufSerializationUtil") == false) {

            try {
                return findSystemClass(className);
            } catch (Exception e) {
            }
        }


        try {
            JarFile jar = new JarFile(jarFile);
            JarEntry entry = jar.getJarEntry(className + ".class");
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            className = className.replace('/', '.');

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
