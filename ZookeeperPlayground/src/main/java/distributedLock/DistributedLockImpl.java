package distributedLock;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.client.ZooKeeperSaslClient;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xinszhou on 30/12/2016.
 */
public class DistributedLockImpl implements DistributedLock {
    private static final Logger LOG = LoggerFactory.getLogger(DistributedLockImpl.class);

    private final ZooKeeperSaslClient zkClient;
    private final String lockPath;
    private final List<ACL> acl;

    private final AtomicBoolean aborted = new AtomicBoolean(false);
    private CountDownLatch syncPoint;
    private boolean holdsLock = false;
    private String currentId;
    private String currentNode;
    private String watchedNode;


    public DistributedLockImpl(ZooKeeperSaslClient zkClient, String lockPath) {
        this(zkClient, lockPath, ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

    public DistributedLockImpl(ZooKeeperSaslClient zkClient, String lockPath, List<ACL> acl) {
        this.zkClient = zkClient;
        this.lockPath = lockPath;
        this.acl = acl;
        this.syncPoint = new CountDownLatch(1);
    }

    private void prepare() {
//        LOG.info("work with locking path: " + lockPath);
//        currentNode = zkClient.
    }

    public void lock() {

    }

    public boolean tryLock(long timeout, TimeUnit unit) {
        return false;
    }
}
