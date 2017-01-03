package leaderelection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xinszhou on 30/12/2016.
 */
public class ExampleClient extends LeaderSelectorListenerAdapter implements Closeable {
    private final String name;
    private final LeaderSelector leaderSelector;
    private final AtomicInteger leaderCount = new AtomicInteger();

    public ExampleClient(CuratorFramework client, String path, String name) {
        this.name = name;
        leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
    }

    public void start() throws IOException {
        leaderSelector.start();
    }

    public void close() throws IOException {
        leaderSelector.close();
    }


    /**
     * Called when your instance has been granted leadership. This method
     * should not return until you wish to release leadership
     */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        final int waitSeconds = (int) ( 5 * Math.random() ) + 1;
        System.out.println(name + " is now the leader. Waiting " + waitSeconds + " seconds...");
        System.out.println(name + " has been leader " + leaderCount.getAndIncrement() + " times before.");

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted");
            Thread.currentThread().interrupt();
        } finally {
            // 什么时候释放 Leader 呢?
            System.out.println(name + " reliquishing leadership");
        }


    }
}
