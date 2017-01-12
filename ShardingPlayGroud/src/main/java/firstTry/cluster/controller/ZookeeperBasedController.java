package firstTry.cluster.controller;

import firstTry.cluster.RegionServer.ClusterRegionServer;
import firstTry.core.DefaultShardAllocationStrategy;
import firstTry.core.ShardAllocationStrategy;
import firstTry.core.controller.Controller;
import firstTry.core.networking.EndPoint;
import firstTry.core.shard.RegionServer;
import firstTry.core.shard.Shard;
import firstTry.core.shard.TableAndShard;
import firstTry.core.table.TableMetaData;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.EOFException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xinszhou on 09/01/2017.
 */
public class ZookeeperBasedController implements Controller {

    ShardAllocationStrategy shardAllocationStrategy;
    Map<TableAndShard, Shard> regionServerManager;
    List<RegionServer> regionServers;
    String ZookeeperConnection;
    CuratorFramework client;
    LeaderLatch leaderLatch;

    public ZookeeperBasedController(String host, String port, CuratorFramework _client) {
        // for debug
        if (_client != null) this.client = _client;
        else {
            client = CuratorFrameworkFactory.newClient(ZookeeperConnection, new ExponentialBackoffRetry(1000, 3));
            client.start();

        }

        shardAllocationStrategy = new DefaultShardAllocationStrategy();
        ZookeeperConnection = host + ":" + port;
        regionServerManager = new HashMap<>();
        regionServers = new LinkedList<>();
        leaderLatch = new LeaderLatch(client,
                "/sharding-playground/controller",
                ZookeeperConnection);
    }


    public void start() {
        try {
            leaderLatch.start();
            leaderLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            leaderLatch.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EndPoint getLeader() {

        Participant leader = null;

        try {
            leader = leaderLatch.getLeader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (leader != null) {

            String[] ids = leader.getId().split(":");
            return new EndPoint(ids[0], ids[1]);
        } else {
            System.out.println("leader is not existed");
        }

        return null;
    }

    @Override
    public boolean isLeader() {
        return leaderLatch.hasLeadership();
    }

    @Override
    public void addRegionServer(EndPoint endPoint) {

        if (isLeader()) {
            RegionServer regionServer = new ClusterRegionServer(endPoint.getHost(), endPoint.getPort());
            regionServers.add(regionServer);
        } else {
            System.out.println("not leader, cannot add do it");
        }
    }

    public void addRegionServer(RegionServer regionServer) {
        regionServers.add(regionServer);
    }


    @Override
    public void deleteRegionServer(EndPoint endPoint) {

        if (isLeader()) {
            RegionServer regionServer = new ClusterRegionServer(endPoint.getHost(), endPoint.getPort());
            // compare function already overrided
            regionServers.remove(regionServer);
        } else {
            System.out.println("not leader, cannot delete region server");
        }
    }

    public void deleteRegionServer(RegionServer regionServer) {
        regionServers.remove(regionServer);
    }

    //@todo update zookeeper
    @Override
    public void createTable(String tableName, int shardNumber, int replicationFactory) {
        if (isLeader()) {
            synchronized (this) {
                // protect region servers
                shardAllocationStrategy.allocateShard(regionServers, tableName, shardNumber, replicationFactory);
            }

        } else {
            reportError();
        }
    }

    // @todo update zookeeper
    @Override
    public void deleteTable(String tableName) {
        if (isLeader()) {
            synchronized (this) {
                regionServers.forEach(regionServer -> {
                    List<Shard> shards = regionServer.getShards();

                    List<Shard> dropped = shards.stream()
                            .filter(shard -> shard.getTableName() == tableName)
                            .collect(Collectors.toList());

                    shards.removeAll(dropped);

                });
            }
        } else {
            reportError();
        }
    }

    @Override
    public TableMetaData fetchClusterMetaInfo(String tableName) {
        return null;
    }

    private void reportError() {
        System.out.println("not leader cannot do it");
    }

}
