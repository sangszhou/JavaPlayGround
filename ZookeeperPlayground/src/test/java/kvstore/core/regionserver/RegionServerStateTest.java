package kvstore.core.regionserver;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.zookeeper.server.util.SerializeUtils;
import org.junit.Test;
import rpcSupport.codec.ProtoBufSerializationUtil;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class RegionServerStateTest {
    @Test
    public void serialize() throws Exception {
        RegionServerState regionServerState = new RegionServerState("","host", 100);
        System.out.println("size: " + regionServerState.managedTables.size());
        byte[] inBytes = SerializationUtils.serialize(regionServerState);

        RegionServerState cpRegionServerState = SerializationUtils.deserialize(inBytes);
        cpRegionServerState.managedTables.size();
    }

}