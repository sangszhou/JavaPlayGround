package kvstore.core.regionserver;

import framework.CreateClientExample;
import kvstore.core.Table.InMemoryTable;
import kvstore.core.Table.Table;
import kvstore.core.protocol.ClusterInfo;
import kvstore.core.regionserver.tableEvent.TableEvent;
import kvstore.core.regionserver.tableEvent.TableMetadataChangeEvent;
import kvstore.core.regionserver.tableEvent.TableOfflineEvent;
import kvstore.core.regionserver.tableEvent.TableOnlineEvent;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class InMemoryRegionServer implements RegionServer {

    Map<String, Table> managedTables = new HashMap<>();

    BlockingQueue<TableEvent> eventQueue;

    String regionServerId;
    String host;
    int port;

    RegionServerState regionServerState;

    public InMemoryRegionServer(String regionServerId, String host, int port, String zkConnectionString) {
        this.regionServerId = regionServerId;
        this.host = host;
        this.port = port;
        this.zkConnectionString = zkConnectionString;
        regionServerState = new RegionServerState(regionServerId, host, port);
    }

    //internal
    private CuratorFramework zkClient;
    String zkConnectionString;
    String zkPath;

    @Override
    public String getId() {
        return regionServerId;
    }

    @Override
    public String setId(String id) {
        this.regionServerId = id;
        return regionServerId;
    }

    @Override
    public void init() {
        //register self to zk
        System.out.println("register regionServer to zk before providing service");

        zkClient = CreateClientExample.createSimple(zkConnectionString);
        zkClient.start();
        zkPath = ClusterInfo.zkRegionServerPath + '/' + getId();

        byte[] stateInBytes = SerializationUtils.serialize(regionServerState);

        try {
            zkClient.create().creatingParentsIfNeeded().forPath(zkPath, stateInBytes);
        } catch (Exception e) {
            System.out.println("failed to register self to zk");
            e.printStackTrace();
        }
        eventQueue = new LinkedBlockingDeque<>();

        // handle the coordination event
        new Thread(() -> {
            new RegionServerListener(eventQueue, zkClient, regionServerId).start();
        }).start();

    }

    @Override
    public void process(TableEvent tableEvent) {
        synchronized (this) {
            if (tableEvent instanceof TableOnlineEvent) {
                TableOnlineEvent tableOnlineEvent = (TableOnlineEvent) tableEvent;
                System.out.println("new table assigned to this region server");

                try {
                    registerTable(tableOnlineEvent.getTableMetaData().getTableName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (tableEvent instanceof TableOnlineEvent) {
                TableOfflineEvent tableOfflineEvent = (TableOfflineEvent) tableEvent;
                System.out.println("table is deleted from this region server");

                try {
                    removeTable(tableOfflineEvent.getTableMetaData().getTableName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (tableEvent instanceof TableMetadataChangeEvent) {
                TableMetadataChangeEvent tableMetadataChangeEvent = (TableMetadataChangeEvent) tableEvent;
                System.out.println("do not handle TableMetadataChangeEvent for now");
            }
        }
    }

    @Override
    public void put(String tableName, String key, String value) throws Exception {
        checkIfTableExist(tableName);
        Table table = managedTables.get(tableName);
        table.put(key, value);
    }

    @Override
    public void delete(String tableName, String key) throws Exception {
        checkIfTableExist(tableName);
        Table table = managedTables.get(tableName);
        table.delete(key);
    }

    @Override
    public String get(String tableName, String key) throws Exception {
        checkIfTableExist(tableName);
        Table table = managedTables.get(key);
        return table.get(key);
    }

    @Override
    public void registerTable(String tableName) throws Exception {
        synchronized (this) {
            if (managedTables.get(tableName) != null) {
                System.out.println("the table has already been registered in this region server, don't need to register again");
            } else {
                managedTables.put(tableName, new InMemoryTable(tableName));
                regionServerState.addTable(tableName);
                zkClient.setData().forPath(zkPath, SerializationUtils.serialize(regionServerState));
            }
        }

    }

    @Override
    public void removeTable(String tableName) throws Exception {
        synchronized (this) {
            checkIfTableExist(tableName);
            managedTables.remove(tableName);
        }
    }

    void checkIfTableExist(String tableName) throws Exception {
        if (managedTables.get(tableName) == null) {
            throw new Exception("Table is not here");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                TableEvent event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
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
