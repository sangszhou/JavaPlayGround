package debug;

/**
 * Created by xinszhou on 14/12/2016.
 */
public class MyThread implements Runnable {

    @Override
    public void run() {
        synchronized (this) {
            for(int i = 0; i < 1; i --) {
                System.out.println(Thread.currentThread().getName() + " synchronized loop " + i);
            }
        }
    }

    public static void main(String args[]) {
        MyThread t1 = new MyThread();
        Thread ta = new Thread(t1, "A");
        Thread tb = new Thread(t1, "B");

        ta.start();
        tb.start();
    }

}
