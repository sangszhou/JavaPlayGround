package firstTry.core.shard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinszhou on 06/01/2017.
 */
// must be thread safe
/**
 * 能不能直接发送消息给 Shard 呢？ 不使用 TransportLayer 而是使用 AOP, 这样做的好处是轻便且易于 debug
 *
 */

public abstract class Shard {

    String tableName;
    String shardId;
    String primaryShardId;
    boolean isPrimary;

    public Shard(String tableName, String shardId, String primaryShard) {
        this.tableName = tableName;
        this.shardId = shardId;
        this.primaryShardId = primaryShard;
        isPrimary = primaryShard == shardId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setShardId(String shardId) {
        this.shardId = shardId;
    }

    public void setPrimaryShardId(String primaryShardId) {
        this.primaryShardId = primaryShardId;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getTableName() {
        return tableName;
    }

    public String getShardId() {
        return shardId;
    }

    public String getPrimaryShardId() {
        return primaryShardId;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public abstract void put(String key, String value, String version);

    public abstract String get(String key, String version);

    public abstract String delete(String key, String version);

}
