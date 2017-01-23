package kvstore.core.regionserver;

import javafx.scene.control.Tab;
import kvstore.core.Table.TableMetaData;
import kvstore.core.master.masterEvent.MasterEvent;
import kvstore.core.master.masterEvent.RegionServerDataChangeEvent;
import kvstore.core.master.masterEvent.RegionServerOfflineEvent;
import kvstore.core.master.masterEvent.RegionServerOnlineEvent;
import kvstore.core.protocol.ClusterInfo;
import kvstore.core.regionserver.tableEvent.TableEvent;
import kvstore.core.regionserver.tableEvent.TableOfflineEvent;
import kvstore.core.regionserver.tableEvent.TableOnlineEvent;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class RegionServerListener {

    BlockingQueue<TableEvent> eventQueue;
    PathChildrenCache regionServerCache;
    CuratorFramework zkClient;
    String regionServerId;


    public RegionServerListener(BlockingQueue<TableEvent> eventQueue, CuratorFramework zkClient, String regionServerId) {
        this.eventQueue = eventQueue;
        this.zkClient = zkClient;
        this.regionServerId = regionServerId;
        System.out.println("region server listen to " + ClusterInfo.zkRegionServerPath + "/" + regionServerId);
        regionServerCache = new PathChildrenCache(zkClient, ClusterInfo.zkRegionServerPath + "/" + regionServerId, true);
    }

    void start() {
        System.out.println("path children cache listener inited");

        PathChildrenCacheListener cacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("child event triggered");
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("child added");
                        String[] paths = event.getData().getPath().split("/");
                        String tableName = paths[paths.length - 1];

                        TableMetaData tableMetaData = new TableMetaData(0,
                                0,
                                tableName,
                                true);

                        TableEvent tableEvent = new TableOnlineEvent(tableMetaData);
                        eventQueue.add(tableEvent);
                        break;

                    case CHILD_UPDATED:
                        System.out.println("table event child updated, ignore this for now, may use this info to enable replica");
                        break;

                    case CHILD_REMOVED:
                        System.out.println("child removed");
                        TableMetaData tableMetaData1 = new TableMetaData(0, 0, getNameFromPath(event.getData().getPath()), true);
                        TableEvent removeEvent = new TableOfflineEvent(tableMetaData1);
                        eventQueue.add(removeEvent);
                        break;
                }
            }
        };

        regionServerCache.getListenable().addListener(cacheListener);

        try {
            // don't forget to trigger the event
            regionServerCache.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNameFromPath(String path) {
        String[] paths = path.split("/");
        return paths[paths.length - 1];
    }
}
