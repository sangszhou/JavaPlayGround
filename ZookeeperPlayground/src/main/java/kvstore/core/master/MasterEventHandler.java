package kvstore.core.master;

import kvstore.core.master.masterEvent.MasterEvent;
import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xinszhou on 18/01/2017.
 * @todo should use handler instead of this one
 */
public abstract class MasterEventHandler implements Runnable {

    BlockingQueue<MasterEvent> eventQueue;

    public MasterEventHandler(BlockingQueue<MasterEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void run() {
//        while (true) {
//            try {
//                MasterEvent event = eventQueue.poll(500, TimeUnit.MILLISECONDS);
//                if (event != null) {
//                    process(event);
//                }
//
//            } catch (InterruptedException e) {
//                System.out.println("exception in run method of MasterEventHandler");
//                e.printStackTrace();
//            }
//        }
    }


    public abstract void process(MasterEvent masterEvent );

}
