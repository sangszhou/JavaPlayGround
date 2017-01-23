package kvstore.core.master.eventListener;

import kvstore.core.master.masterEvent.MasterEvent;
import kvstore.core.master.masterEvent.RegionServerDataChangeEvent;
import kvstore.core.master.masterEvent.RegionServerOfflineEvent;
import kvstore.core.master.masterEvent.RegionServerOnlineEvent;
import kvstore.core.protocol.ClusterInfo;
import kvstore.core.regionserver.RegionServerState;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class MasterRegionServerListener {

    BlockingQueue<MasterEvent> eventQueue;
    PathChildrenCache regionServerCache;
    CuratorFramework zkClient;

    public MasterRegionServerListener(BlockingQueue<MasterEvent> eventQueue, CuratorFramework zkClient) {
        this.eventQueue = eventQueue;
        this.zkClient = zkClient;
        regionServerCache = new PathChildrenCache(zkClient, ClusterInfo.zkRegionServerPath, true);
    }

    void start() {
        PathChildrenCacheListener cacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("事件类型：" + event.getType());
                System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
                RegionServerState regionServerState = SerializationUtils.deserialize(event.getData().getData());

                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("child added");
                        MasterEvent onlineEvent = new RegionServerOnlineEvent(regionServerState);
                        eventQueue.add(onlineEvent);
                        break;

                    case CHILD_UPDATED:
                        System.out.println("child updated");
                        MasterEvent dataChangeEvent = new RegionServerDataChangeEvent(regionServerState);
                        eventQueue.add(dataChangeEvent);
                        break;
                    case CHILD_REMOVED:
                        System.out.println("child removed");
                        MasterEvent offlineEvent = new RegionServerOfflineEvent(regionServerState);
                        eventQueue.add(offlineEvent);
                        break;
                }
            }
        };
        regionServerCache.getListenable().addListener(cacheListener);
    }


}
