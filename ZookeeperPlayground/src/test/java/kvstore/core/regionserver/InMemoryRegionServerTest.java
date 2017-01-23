package kvstore.core.regionserver;

import framework.CreateClientExample;
import kvstore.core.protocol.ClusterInfo;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.Test;
import rpcSupport.codec.ProtoBufSerializationUtil;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class InMemoryRegionServerTest {
    String host = "localhost";
    int port = 10000;

    @Test
    public void registerSelfToZk() throws Exception {
        TestingServer zkServer = new TestingServer();
        RegionServer regionServer = new InMemoryRegionServer("regionserver-01", host, port, zkServer.getConnectString());

        System.out.println(regionServer.getId());
        regionServer.init();

        CuratorFramework zkClient = CreateClientExample.createSimple(zkServer.getConnectString());
        zkClient.start();

        byte[] state = zkClient.getData().forPath("/kvstore/regionserver/" + regionServer.getId());

        RegionServerState regionServerState = SerializationUtils.deserialize(state);

        System.out.println("host: " + regionServerState.getHost());
        System.out.println("port: " + regionServerState.getPort());
        regionServerState.managedTables.forEach(tableName -> System.out.println("managed table: " + tableName));

        regionServer.registerTable("newTable");

        state = zkClient.getData().forPath("/kvstore/regionserver/" + regionServer.getId());

        regionServerState = SerializationUtils.deserialize(state);
        System.out.println("host: " + regionServerState.getHost());
        System.out.println("port: " + regionServerState.getPort());
        regionServerState.managedTables.forEach(tableName -> System.out.println("managed table: " + tableName));
    }


    @Test
    public void addPathAfterDataNode() throws Exception{
        TestingServer zkServer = new TestingServer();
        CuratorFramework zkClient = CreateClientExample.createSimple(zkServer.getConnectString());
        zkClient.start();

        zkClient.create().creatingParentsIfNeeded().forPath("/test", "test".getBytes());
        zkClient.create().creatingParentsIfNeeded().forPath("/test/deeper", "deeper".getBytes());

        String data = new String(zkClient.getData().forPath("/test/deeper"));
        System.out.println(data);
    }

    @Test
    public void testRegionServeListener() throws Exception {
        TestingServer zkServer = new TestingServer();
        String regionServerId = "region-1";
        RegionServer regionServer = new InMemoryRegionServer(regionServerId, host, port, zkServer.getConnectString());
        regionServer.init();


        //create a path and see region server's response
        CuratorFramework zkClient = CreateClientExample.createSimple(zkServer.getConnectString());
        zkClient.start();

        String path = ClusterInfo.zkRegionServerPath + "/" + regionServerId + "/" + "table_1";
        System.out.println("create path " + path);

        zkClient.create().creatingParentsIfNeeded().forPath(path);

        Thread.sleep(3000);

    }

}