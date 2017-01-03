package distributedLock;

import java.util.concurrent.TimeUnit;

/**
 * Created by xinszhou on 30/12/2016.
 */
public interface DistributedLock {
    void lock();
    boolean tryLock(long timeout, TimeUnit unit);

    public static class LockingException extends RuntimeException {
        public LockingException(String msg, Exception e) {
            super(msg, e);
        }

        public LockingException(String msg) {
            super(msg);
        }
    }
}
