package kvstore.cluster.master;

import kvstore.core.master.Master;
import kvstore.core.master.MasterEventHandler;
import kvstore.core.master.masterEvent.MasterEvent;
import kvstore.core.master.masterEvent.RegionServerDataChangeEvent;
import kvstore.core.master.masterEvent.RegionServerOfflineEvent;
import kvstore.core.master.masterEvent.RegionServerOnlineEvent;
import kvstore.core.protocol.ClusterInfo;
import kvstore.core.regionserver.RegionServerState;
import org.apache.curator.framework.CuratorFramework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class ZkMaster implements Master {

    CuratorFramework zkClient;
    BlockingQueue<MasterEvent> eventQueue = new LinkedBlockingDeque<>();

    Map<String, RegionServerState> clusterMetaInfo;
    List<RegionServerState> onlineRegionServers;

    public ZkMaster(CuratorFramework zkClient) {
        this.zkClient = zkClient;
        clusterMetaInfo = new HashMap<>();
        onlineRegionServers = new LinkedList<>();
    }

    @Override
    public void init() {
    }

    @Override
    public void process(MasterEvent masterEvent) {
        synchronized (this) {
            if (masterEvent instanceof RegionServerOnlineEvent) {
                RegionServerOnlineEvent event = (RegionServerOnlineEvent) masterEvent;
                onlineRegionServers.add(event.getRegionServerState());
            } else if (masterEvent instanceof RegionServerOfflineEvent) {
                RegionServerOfflineEvent event = (RegionServerOfflineEvent) masterEvent;
                onlineRegionServers.remove(event);

                // is this data needed on master, region server data change because master tell it to
            } else if (masterEvent instanceof RegionServerDataChangeEvent) {
                System.out.println("ignore data change for now");
                RegionServerDataChangeEvent event = (RegionServerDataChangeEvent) masterEvent;
                // update meta data, since onlineRegionServers and clusterMetaInfo share the same reference, we can
                // update the onlineRegionServers and clusterMetaInfo will be changed accordingly
                RegionServerState localState = null;
                for (int i = 0; i < onlineRegionServers.size(); i++) {
                    if (onlineRegionServers.get(i) == event.getRegionServerState()) {
                        localState = onlineRegionServers.get(i);
                        break;
                    }
                }

                // update master in memory data
                localState.setManagedTables(event.getRegionServerState().getManagedTables());
                for (int i = 0; i < localState.getManagedTables().size(); i++) {
                    clusterMetaInfo.put(localState.getManagedTables().get(i), localState);
                }

            } else {
                System.out.println("unknown event");
            }
        }

    }

    @Override
    public boolean createTable(String tableName) throws Exception {
        synchronized (this) {
            // find the most available region server
            if (clusterMetaInfo.get(tableName) != null) {
                throw new Exception("the table has already been created");
            }

            int minTableNumOnServer = 999;
            RegionServerState choosenServer = null;
            for (RegionServerState serverState : onlineRegionServers) {
                if (serverState.getManagedTables().size() < minTableNumOnServer) {
                    minTableNumOnServer = serverState.getManagedTables().size();
                    choosenServer = serverState;
                }
            }

            // send create table request to that server
            // let the region server knows
            zkClient.create()
                    .creatingParentsIfNeeded()
                    .forPath(ClusterInfo.zkRegionServerPath + "/" + choosenServer.getId() + "/" + tableName);

            // @todo the best time to update metadata should after region server successfully respond to it
            // this can be achieved by listen to region server data change event, but for now, don't consider that

            return true;
        }
    }

    @Override
    public boolean deleteTable(String tableName) throws Exception {
        synchronized (this) {
            if (clusterMetaInfo.get(tableName) == null) {
                throw new Exception("cannot delete a non-existing table");
            }
            RegionServerState chosenServer = clusterMetaInfo.get(tableName);
            zkClient.delete().forPath(ClusterInfo.zkRegionServerPath + "/" + chosenServer.getId() + "/" + tableName);
            return true;
        }
    }

    @Override
    public RegionServerState getTableInfo(String tableName) throws Exception {
        synchronized (this) {
            if (clusterMetaInfo.get(tableName) == null) {
                throw new Exception("table not exist");
            }
            return clusterMetaInfo.get(tableName);

        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                MasterEvent event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                if (event != null) {
                    process(event);
                }

            } catch (InterruptedException e) {
                System.out.println("exception in run method of MasterEventHandler");
                e.printStackTrace();
            }
        }
    }
}
