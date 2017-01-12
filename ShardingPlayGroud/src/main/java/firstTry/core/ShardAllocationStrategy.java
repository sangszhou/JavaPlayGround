package firstTry.core;

import firstTry.core.shard.RegionServer;
import firstTry.core.shard.Shard;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xinszhou on 06/01/2017.
 */
public interface ShardAllocationStrategy {
    List<RegionServer> allocateShard(List<RegionServer> regionServers,
                                     String tableName,
                                     int shardNum, int replicationFactor);


}
