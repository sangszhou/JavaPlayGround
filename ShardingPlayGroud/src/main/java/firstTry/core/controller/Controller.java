package firstTry.core.controller;

import firstTry.core.networking.EndPoint;
import firstTry.core.table.TableMetaData;

/**
 * Created by xinszhou on 06/01/2017.
 * <p>
 * handle
 * createTable(tableName, replicaFactor)
 * deleteTable(tableName)
 *
 * client 维持 <TableAndShard, Endpoint> 的连接
 */
public interface Controller {

    EndPoint getLeader();

    boolean isLeader();

    // below two methods might useless
    void addRegionServer(EndPoint endPoint);

    void deleteRegionServer(EndPoint endPoint);

    void createTable(String tableName, int shardNumber, int replicationFactory);

    void deleteTable(String tableName);

    TableMetaData fetchClusterMetaInfo(String tableName);

}
