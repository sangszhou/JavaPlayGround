package firstTry.core.shard;

/**
 * Created by xinszhou on 09/01/2017.
 */
public abstract class InternalShard extends Shard {


    public InternalShard(String tableName, String shardId, String primaryShard) {
        super(tableName, shardId, primaryShard);
    }
}
