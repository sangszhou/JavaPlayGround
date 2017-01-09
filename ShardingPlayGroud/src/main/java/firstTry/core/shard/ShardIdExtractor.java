package firstTry.core.shard;

/**
 * Created by xinszhou on 06/01/2017.
 */
public interface ShardIdExtractor<T> {
    String extract(T t);
}
