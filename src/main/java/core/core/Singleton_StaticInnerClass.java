package core.core;

/**
 * Created by xinszhou on 16/02/2017.
 */

public class Singleton_StaticInnerClass {

    private static class SingletonHolder {
        private static final Singleton_StaticInnerClass INSTANCE = new Singleton_StaticInnerClass();
    }

    private Singleton_StaticInnerClass() {
    }

    public static Singleton_StaticInnerClass getInstance() {
        return SingletonHolder.INSTANCE;    // 只有在调用 getInstance() 时，对象才会被创建，同时没有性能缺点，也不依赖 Java 版本
    }

}
