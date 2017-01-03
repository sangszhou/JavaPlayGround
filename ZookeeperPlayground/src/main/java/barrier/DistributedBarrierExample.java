package barrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import java.security.KeyStore;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xinszhou on 30/12/2016.
 */


/**
 * memberQty是成员数量，当enter方法被调用时，成员被阻塞，直到所有的成员都调用了enter。 当leave方法被调用时，它也阻塞调用线程， 知道所有的成员都调用了leave。就像百米赛跑比赛， 发令枪响， 所有的运动员开始跑，等所有的运动员跑过终点线，比赛才结束。
 */
public class DistributedBarrierExample {
    private static final int QTY = 5;
    private static final String PATH = "/example/barrier";

    public static void main(String args[]) {
        try {
            // internal running zookeeper service
            TestingServer server = new TestingServer();
            CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
            client.start();

            ExecutorService executorService = Executors.newFixedThreadPool(QTY);

            for(int i = 0; i < QTY; i ++) {
                // barrier path, number of member qty???
                final DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, PATH, QTY);
                final int index = i;

                Callable<Void> task = new Callable<Void>() {
                    public Void call() throws Exception {
                        Thread.sleep((long) (3 * Math.random()));
                        System.out.println("Client #" + index + " enters");
                        barrier.enter();
                        Thread.sleep((long) (3000 * Math.random()));
                        barrier.leave();
                        System.out.println("Client #" + index + " left");
                        return null;
                    }
                };
                executorService.submit(task);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
