package firstTry.core.shard;


/**
 * Created by xinszhou on 09/01/2017.
 */
public class RemoteShard extends Shard {

    public RemoteShard(String tableName, String shardId, String primaryShard) {
        super(tableName, shardId, primaryShard);
    }

    @Override
    public void put(String key, String value, String version) {

    }

    @Override
    public String get(String key, String version) {
        return null;
    }

    @Override
    public String delete(String key, String version) {
        return null;
    }


}
