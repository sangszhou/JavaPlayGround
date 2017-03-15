package classloader.restore;

import classloader.LoadClassFromExternal;

import java.lang.reflect.Method;

/**
 * Created by xinszhou on 15/03/2017.
 * load class from remote server
 */
public class ReflectFromExternal {

    public static void main(String args[]) throws Exception {
        LoadClassFromExternal external = new LoadClassFromExternal();
        String intMathClz = "com.google.common.math.IntMath";
        Class cl = external.findClassFromRemoteGuavaJar(intMathClz);

//        for (Method method : cl.getMethods()) {
//            System.out.println(method.getName());
//        }

        Method method = cl.getMethod("isPowerOfTwo", int.class);
        boolean result = (boolean) method.invoke(null, 12);

        System.out.println(result);
    }
}
