package firstTry.core;

/**
 * Created by xinszhou on 06/01/2017.
 */
public interface ShardAllocationStrategy {
    void allocateShard(String shardId);
    void rebalance();
}
