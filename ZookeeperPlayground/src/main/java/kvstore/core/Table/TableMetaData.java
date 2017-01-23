package kvstore.core.Table;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class TableMetaData {

    int shardId;
    int replicaId;
    String tableName;
    boolean isLeader;

    public TableMetaData(int shardId, int replicaId, String tableName, boolean isLeader) {
        this.shardId = shardId;
        this.replicaId = replicaId;
        this.tableName = tableName;
        this.isLeader = isLeader;
    }

    public int getShardId() {
        return shardId;
    }

    public int getReplicaId() {
        return replicaId;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setShardId(int shardId) {
        this.shardId = shardId;
    }

    public void setReplicaId(int replicaId) {
        this.replicaId = replicaId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}
