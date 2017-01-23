package kvstore.core.master;

import kvstore.core.master.masterEvent.MasterEvent;
import kvstore.core.protocol.ClusterInfo;
import kvstore.core.regionserver.RegionServerState;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xinszhou on 18/01/2017.
 */
public interface Master extends Runnable{

    void init();

    void process(MasterEvent masterEvent);

    boolean createTable(String tableName) throws Exception;

    boolean deleteTable(String tableName) throws Exception;

    RegionServerState getTableInfo(String tableName) throws Exception;
}
