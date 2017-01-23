package threading;

import java.util.concurrent.CountDownLatch;

/**
 * Created by xinszhou on 01/01/2017.
 */
public class FinishingTestComparation {

    public static void main(String args[]) {
        int n = 2;
        CountDownLatch latch = new CountDownLatch(1*n);
        int task = 1000000000/n;


        Thread t2 = new Thread(new Worker(task, latch));
        Thread t1 = new Thread(new Worker(task, latch));

        System.out.println(System.currentTimeMillis());
        t1.start();
        t2.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis());

    }

    static class Worker implements Runnable {

        int count = 0;
        CountDownLatch latch;

        public Worker(int task, CountDownLatch latch) {
            count = task;
            this.latch = latch;
        }

        @Override
        public void run() {
            while(count > 0) {
                count --;
            }
            latch.countDown();
        }
    }


}
