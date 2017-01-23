package kvstore.core.regionserver.tableEvent;

import kvstore.core.Table.TableMetaData;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class TableOnlineEvent implements TableEvent {

    TableMetaData tableMetaData;

    public TableOnlineEvent(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }
}
