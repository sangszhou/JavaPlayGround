package framework.caches;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

/**
 * Created by xinszhou on 19/01/2017.
 *
 * @fixme Not working 必须得把 Thread.sleep 事件延长才能看到效果
 */
public class NodeCacheTest {
        private static final String PATH = "/example/cache";

        public static void main(String[] args) throws Exception {
            TestingServer zkServer = new TestingServer();

            CuratorFramework client = CuratorFrameworkFactory.newClient(zkServer.getConnectString(),
                    new ExponentialBackoffRetry(1000, 3));

            client.start();

            final NodeCache cache = new NodeCache(client, PATH);
            cache.start();

            NodeCacheListener listener = new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    ChildData data = cache.getCurrentData();
                    if (null != data) {
                        System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
                    } else {
                        System.out.println("节点被删除!");
                    }
                }
            };

            cache.getListenable().addListener(listener);

            client.create().creatingParentsIfNeeded().forPath(PATH, "01".getBytes());
            Thread.sleep(1000);

            client.setData().forPath(PATH, "02".getBytes());
            Thread.sleep(1000);

            client.delete().deletingChildrenIfNeeded().forPath(PATH);
            Thread.sleep(1000 * 2);

            cache.close();
            client.close();

            System.out.println("OK!");
        }
}
