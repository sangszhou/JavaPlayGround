package firstTry.core.table;

import firstTry.core.networking.EndPoint;
import firstTry.core.shard.TableAndShard;

import java.util.Map;
import java.util.Set;

/**
 * Created by xinszhou on 09/01/2017.
 */
public class TableMetaData {

    int shardNum;
    int replicationFactor;
    int tableName;

    public TableMetaData(int shardNum, int replicationFactor, int tableName) {
        this.shardNum = shardNum;
        this.replicationFactor = replicationFactor;
        this.tableName = tableName;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public void setTableName(int tableName) {
        this.tableName = tableName;
    }

    public int getShardNum() {
        return shardNum;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public int getTableName() {
        return tableName;
    }
}
