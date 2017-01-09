package firstTry.core.shard;

import firstTry.core.model.EntityIdExtractor;

/**
 * Created by xinszhou on 06/01/2017.
 */

/**
 * 负责一部分数据
 * @param <T>
 */
public interface RegionServer<T> {

    void handleMessage(T t);

}
