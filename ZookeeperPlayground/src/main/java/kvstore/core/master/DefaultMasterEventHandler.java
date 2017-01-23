package kvstore.core.master;

import kvstore.core.master.masterEvent.MasterEvent;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class DefaultMasterEventHandler extends MasterEventHandler {

    public DefaultMasterEventHandler(BlockingQueue<MasterEvent> eventQueue) {
        super(eventQueue);
    }

    @Override
    public void process(MasterEvent masterEvent) {

    }
}
