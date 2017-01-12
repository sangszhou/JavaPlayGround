package util;

import firstTry.core.shard.RegionServer;

import java.util.List;

/**
 * Created by xinszhou on 09/01/2017.
 */
public class ShowInfo {

    public static void showRegionServes(List<RegionServer> regionServers) {
        regionServers.forEach(regionServer -> {
            System.out.println("region server: ");
            regionServer.getShards().forEach(shard -> {
                System.out.println("shard: " +
                        shard.getTableName() + " " +
                        shard.getShardId() + " primaryShardId:" +
                        shard.getPrimaryShardId());
            });
        });

    }
}
