package firstTry.core.shard;

/**
 * Created by xinszhou on 06/01/2017.
 */

import java.util.List;
import java.util.Set;

/**
 * 代表一个 server, 负责一部分数据， 某些 table 的某些 shard
 * 可以使用
 * // 与 zookeeper 建立连接
 */
public interface RegionServer extends Comparable<RegionServer> {

    String getId();
    String setId();

    List<Shard> getShards();

    TableAndShard getRegionServerInfo();

    void put(String table, String key, String value, String version);

    String get(String table, String key, String version);

    String delete(String table, String key, String version);

}
