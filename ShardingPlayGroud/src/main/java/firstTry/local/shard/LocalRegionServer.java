package firstTry.local.shard;

import com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore;
import firstTry.core.data.DataStore;
import firstTry.core.model.TableAndShard;
import firstTry.core.shard.AbstractRegionServer;

import java.util.Map;

/**
 * Created by xinszhou on 06/01/2017.
 */
public class LocalRegionServer extends AbstractRegionServer {

    Map<TableAndShard, DataStore> knownData;

    @Override
    public void handleMessage(Object o) {

    }
}
