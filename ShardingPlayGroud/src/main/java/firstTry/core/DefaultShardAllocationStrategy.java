package firstTry.core;

import firstTry.core.shard.RegionServer;
import firstTry.core.shard.RemoteShard;
import firstTry.core.shard.Shard;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by xinszhou on 06/01/2017.
 */
public class DefaultShardAllocationStrategy implements ShardAllocationStrategy {

    /**
     * algorithm
     * regionServer r1, r2, r3
     * table t1, shard s1, s2
     * t1%n -> start offset for s1, set s2 at the
     *
     * @param regionServers
     * @return
     */
    @Override
    public List<RegionServer> allocateShard(List<RegionServer> regionServers,
                                            String tableName,
                                            int shardNum,
                                            int replicationFactor) {

        int startOffset = Math.abs(UUID.randomUUID().hashCode() % regionServers.size());

        for (int i = 0; i < shardNum; i++) {
            Shard primaryShard = new RemoteShard(tableName, i + "-0", i + "-0");
            int offset = (startOffset + i) % regionServers.size();
            regionServers.get(offset).getShards().add(primaryShard);

            for (int j = 1; j < replicationFactor; j++) {
                Shard replicaShard = new RemoteShard(tableName, i + "-" + j, i + "-" + j);
                offset = (startOffset + i + j) % regionServers.size();
                regionServers.get(offset).getShards().add(replicaShard);
            }
        }

        return regionServers;

    }
}
