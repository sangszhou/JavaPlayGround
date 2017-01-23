package kvstore.core.master.masterEvent;

import kvstore.core.regionserver.RegionServerState;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class RegionServerOfflineEvent implements MasterEvent {
    RegionServerState regionServerState;


    public RegionServerOfflineEvent(RegionServerState regionServerState) {
        this.regionServerState = regionServerState;
    }

    public RegionServerState getRegionServerState() {
        return regionServerState;
    }

    public void setRegionServerState(RegionServerState regionServerState) {
        this.regionServerState = regionServerState;
    }
}
