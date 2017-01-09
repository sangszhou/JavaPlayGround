package firstTry.core.shard;

import firstTry.core.networking.Replica;

import java.util.Map;
import java.util.Set;

/**
 * Created by xinszhou on 06/01/2017.
 */
public class Shard {
    String tableName;
    String shardId;

    Map<Integer, Replica> followers;



}
