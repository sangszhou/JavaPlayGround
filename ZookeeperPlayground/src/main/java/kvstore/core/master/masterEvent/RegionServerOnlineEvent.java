package kvstore.core.master.masterEvent;

import kvstore.core.master.masterEvent.MasterEvent;
import kvstore.core.regionserver.RegionServerState;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class RegionServerOnlineEvent implements MasterEvent {
    RegionServerState regionServerState;

    public RegionServerState getRegionServerState() {
        return regionServerState;
    }

    public void setRegionServerState(RegionServerState regionServerState) {
        this.regionServerState = regionServerState;
    }

    public RegionServerOnlineEvent(RegionServerState regionServerState) {
        this.regionServerState = regionServerState;
    }

}
