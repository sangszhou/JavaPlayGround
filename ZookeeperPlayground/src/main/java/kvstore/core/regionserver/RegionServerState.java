package kvstore.core.regionserver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinszhou on 18/01/2017.
 */
//@todo version number
public class RegionServerState implements Serializable, Comparable<RegionServerState> {
    String id;
    String host;
    int port;
    List<String> managedTables;

    public RegionServerState(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        managedTables = new LinkedList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addTable(String tableName) {
        managedTables.add(tableName);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setManagedTables(List<String> managedTables) {
        this.managedTables = managedTables;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public List<String> getManagedTables() {
        return managedTables;
    }

    @Override
    public int compareTo(RegionServerState o) {
        if (this.host.compareTo(o.host) != 0) {
            return this.host.compareTo(o.host);
        }
        if (this.port != o.port) {
            return this.port - o.port;
        }
        return -1;
    }
}
