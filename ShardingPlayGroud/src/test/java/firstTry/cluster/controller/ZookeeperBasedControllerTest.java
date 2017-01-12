package firstTry.cluster.controller;

import firstTry.cluster.RegionServer.ClusterRegionServer;
import firstTry.core.networking.EndPoint;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.Test;
import util.ShowInfo;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 09/01/2017.
 */
public class ZookeeperBasedControllerTest {


    @Test
    public void init() throws Exception {

        TestingServer server = new TestingServer();
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
        zkClient.start();
//        zkClient.blockUntilConnected(); // not need this statement
        String host1 = "localhost";
        String port1 = "2181";

        ZookeeperBasedController controller = new ZookeeperBasedController(host1, port1, zkClient);
        controller.start();

        while (true) {
            EndPoint endPoint = controller.getLeader();
            if (endPoint != null) {
                System.out.println("endpoint info: " + endPoint.getHost() + ":" + endPoint.getPort());
                break;
            } else {
                System.out.println("another while true");
                Thread.sleep(50);
            }
        }

    }


    @Test
    public void createTable() throws Exception {
        TestingServer server = new TestingServer();
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
        zkClient.start();
        String host1 = "localhost";
        String port1 = "2181";

        ZookeeperBasedController controller = new ZookeeperBasedController(host1, port1, zkClient);
        controller.start();

        String host2 = "localhost";
        String port2 = "3000";
        ClusterRegionServer regionServer1 = new ClusterRegionServer(host2, port2);
        controller.addRegionServer(regionServer1);

        String host3 = "lcoalhost";
        String port3 = "3001";
        ClusterRegionServer regionServer2 = new ClusterRegionServer(host3, port3);
        controller.addRegionServer(regionServer2);

        String tableName = "xinszhou-table";

        controller.createTable(tableName, 2, 2);

        controller.regionServers.forEach(regionServer -> {
            System.out.println("region server: ");
            regionServer.getShards().forEach(shard -> {
                System.out.println("shard: " +
                        shard.getTableName() + " " +
                        shard.getShardId() + " primaryShardId:" +
                        shard.getPrimaryShardId());
            });
        });


        controller.deleteTable(tableName);
        ShowInfo.showRegionServes(controller.regionServers);
    }


}