package firstTry.cluster.RegionServer;

import firstTry.core.shard.AbstractRegionServer;
import firstTry.core.shard.RegionServer;
import firstTry.core.shard.Shard;
import firstTry.core.shard.TableAndShard;
import firstTry.core.table.TableMetaData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xinszhou on 09/01/2017.
 * <p>
 * connection point with client
 */
public class ClusterRegionServer extends AbstractRegionServer {

    String id;

    String host;
    String port;

    // shards on this node, what if there are two replicas on this node?

    Map<String, List<Shard>> managedTables;
    Map<String, TableMetaData> tablesInfo;
    List<Shard> managedShards;


    public ClusterRegionServer(String host, String port) {
        this.host = host;
        this.port = port;

        managedShards = new LinkedList<>();
        managedTables = new HashMap<>();
        tablesInfo = new HashMap<>();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String setId() {
        return null;
    }

    @Override
    public List<Shard> getShards() {
        return managedShards;
    }

    @Override
    public TableAndShard getRegionServerInfo() {
        return null;
    }

    private Optional<Shard> getTargetShard(String table, String key) {
        return Optional.empty();
    }

    @Override
    public void put(String table, String key, String value, String version) {
        if (managedTables.get(table) != null) {
            List<Shard> shards = managedTables.get(table);
            TableMetaData tableMetaData = tablesInfo.get(table);

            int destShardId = Math.abs(key.hashCode() % tableMetaData.getShardNum()) % tableMetaData.getShardNum();

            List<Shard> primaryShards = shards.stream()
                    .filter(shard -> Integer.valueOf(shard.getShardId()) == destShardId
                            && shard.isPrimary()).collect(Collectors.toList());

            if (primaryShards == null || primaryShards.size() == 0) {
                return;
            } else {
                Shard primaryShard = primaryShards.get(0);
                primaryShard.put(key, value, version);
            }
        } else {
            System.out.println("din't found the shard in region server");
        }


    }

    @Override
    public String get(String table, String key, String version) {
        return null;
    }

    @Override
    public String delete(String table, String key, String version) {
        return null;
    }

    // 难点，Comparable 的 type parameter 问题怎么处理
    @Override
    public int compareTo(RegionServer o) {

        if (o instanceof ClusterRegionServer) {
            ClusterRegionServer temp = (ClusterRegionServer) o;
            return (this.host + this.port).compareTo(temp.host + temp.port);
        }

        return -1;
    }
}
