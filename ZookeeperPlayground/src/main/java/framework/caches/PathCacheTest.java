package framework.caches;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

/**
 * Created by xinszhou on 19/01/2017.
 * <p>
 * note:
 * create path before running the method if using real zookeeper
 */
public class PathCacheTest {
    private static final String PATH = "/example/cache";

    public static void main(String[] args) throws Exception {
        TestingServer zkServer = new TestingServer();
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkServer.getConnectString(),
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();

        PathChildrenCacheListener cacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("事件类型：" + event.getType());
                System.out.println("path: " + event.getData().getPath());
                System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
            }
        };

        cache.getListenable().addListener(cacheListener);

        client.create().creatingParentsIfNeeded().forPath("/example/cache/test01", "01".getBytes());
        Thread.sleep(10);

        client.create().forPath("/example/cache/test02", "02".getBytes());
        Thread.sleep(10);

        client.setData().forPath("/example/cache/test01", "01_V2".getBytes());
        Thread.sleep(10);

        for (ChildData data : cache.getCurrentData()) {
            System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData()));
        }

        client.delete().forPath("/example/cache/test01");
        Thread.sleep(10);

        client.delete().forPath("/example/cache/test02");
        Thread.sleep(1000 * 5);

        cache.close();
        client.close();
        System.out.println("OK!");
    }

}
