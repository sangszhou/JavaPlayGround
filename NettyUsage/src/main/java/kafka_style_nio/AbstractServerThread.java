package kafka_style_nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Selector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xinszhou on 02/12/2016.
 */
public abstract class AbstractServerThread implements Runnable {

    Logger log = LoggerFactory.getLogger(getClass());

    CountDownLatch startupLatch = new CountDownLatch(1);
    CountDownLatch shutdownLatch = new CountDownLatch(1);
    AtomicBoolean alive = new AtomicBoolean(true);

    public abstract boolean wakeup();

    public void shutdown() throws Exception{
        alive.set(false);
        wakeup();
        shutdownLatch.await();
    }

    public void startupComplete() {
        startupLatch.countDown();
    }

    public boolean isRunning() {
        return alive.get();
    }


    @Override
    public void run() {

    }
}
