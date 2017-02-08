package core.concurrent;

/**
 * Created by xinszhou on 01/02/2017.
 */
public class WaitNotify {

    Object st;

    public WaitNotify(Object st) {
        this.st = st;
    }

    public void callAwait() {
        try {
            synchronized (st) {
                st.wait();
            }
            System.out.println("call await successfully");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        WaitNotify wn = new WaitNotify(new Integer(12));

        wn.callAwait();

    }
}
