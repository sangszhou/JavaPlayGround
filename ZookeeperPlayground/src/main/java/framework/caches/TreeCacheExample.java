package framework.caches;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

/**
 * Created by xinszhou on 19/01/2017.
 */
public class TreeCacheExample {
    private static final String PATH = "/example/cache";

    public static void main(String[] args) throws Exception {
        TestingServer zkServer = new TestingServer();

        CuratorFramework client = CuratorFrameworkFactory.newClient(zkServer.getConnectString(),
                new ExponentialBackoffRetry(1000, 3));

        client.start();
        TreeCache cache = new TreeCache(client, PATH);
        cache.start();

        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("事件类型：" + event.getType() + " | 路径：" + event.getData().getPath());
                event.getData().getData();
                // 既有 data: byte[] 又有 type: Node_Add, Node_Removed, Node_Updated
            }
        };

        cache.getListenable().addListener(listener);
        client.create().creatingParentsIfNeeded().forPath("/example/cache/test01/child01");
        client.setData().forPath("/example/cache/test01", "12345".getBytes());
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
        cache.close();
        client.close();
        System.out.println("OK!");
    }
}
