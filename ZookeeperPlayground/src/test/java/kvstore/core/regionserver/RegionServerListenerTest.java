package kvstore.core.regionserver;

import framework.CreateClientExample;
import kvstore.core.protocol.ClusterInfo;
import kvstore.core.regionserver.tableEvent.TableEvent;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class RegionServerListenerTest {

    @Test
    public void testPathChildrenCache() throws Exception {
        BlockingQueue<TableEvent> eventQueue = new LinkedBlockingDeque<>();

        TestingServer zkServer = new TestingServer();
        CuratorFramework zkClient = CreateClientExample.createSimple(zkServer.getConnectString());
        zkClient.start();

        String regionServerId = "region-id";

        RegionServerListener listener = new RegionServerListener(eventQueue, zkClient, regionServerId);
        listener.start();


        zkClient.create().creatingParentsIfNeeded().forPath(ClusterInfo.zkRegionServerPath + "/" + regionServerId);

        Thread.sleep(3000);

    }

}