package firstTry.core.shard;

/**
 * Created by xinszhou on 06/01/2017.
 */
public class TableAndShard {

    /**
     * shardId could be 1, 2, 3, 4
     * cannot change after table created
     */
    String tableName;
    String shardId;

    public TableAndShard(String tableName, String shardId) {
        this.tableName = tableName;
        this.shardId = shardId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setShardId(String shardId) {
        this.shardId = shardId;
    }

    public String getTableName() {
        return tableName;
    }

    public String getShardId() {
        return shardId;
    }
}
