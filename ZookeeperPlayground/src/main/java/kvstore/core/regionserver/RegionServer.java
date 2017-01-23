package kvstore.core.regionserver;

import kvstore.core.regionserver.tableEvent.TableEvent;

/**
 * Created by xinszhou on 18/01/2017.
 */
public interface RegionServer extends Runnable {

    String getId();
    String setId(String id);

    void init();

    void process(TableEvent tableEvent);

    void put(String table, String key, String value) throws Exception;

    void delete(String table, String key) throws Exception;

    String get(String table, String key) throws Exception;

    void registerTable(String tableName) throws Exception;

    void removeTable(String tableName) throws Exception;

}
