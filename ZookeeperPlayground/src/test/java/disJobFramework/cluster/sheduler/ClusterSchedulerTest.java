package disJobFramework.cluster.sheduler;

import disJobFramework.core.scheduler.Scheduler;
import org.junit.Test;
import remote.netty.fileserver.HttpStaticFileServer;
import rpcSupport.server.RPCServer;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 1/17/17.
 */
public class ClusterSchedulerTest {
    String uploadedFileDir = "/ws/github/JavaPlayGround/ZookeeperPlayground/src/main/java/disJobFramework/jarsStorage/schedulerJars/";
    int uploadFilePort = 5566;



    @Test
    public void receiveUploadedFileServer() throws Exception {
        HttpStaticFileServer httpStaticFileServer = new HttpStaticFileServer(uploadedFileDir, uploadFilePort);
        httpStaticFileServer.run();
    }

    @Test
    public void receiveSimpleTaskServer() throws Exception {
        Scheduler clusterScheduler = new ClusterScheduler();
        RPCServer rpcServer = new RPCServer();
        rpcServer.registerBean(Scheduler.class.getName(), clusterScheduler);
        rpcServer.start("10.140.42.170", 7777);
    }

    @Test
    public void receiveJarAndTaskableServer() throws Exception {

        // this one is blocking

        new Thread( () -> {
                HttpStaticFileServer httpStaticFileServer = new HttpStaticFileServer(uploadedFileDir, uploadFilePort);
                try {
                    httpStaticFileServer.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }).start();

        Scheduler clusterScheduler = new ClusterScheduler();
        RPCServer rpcServer = new RPCServer();
        rpcServer.registerBean(Scheduler.class.getName(), clusterScheduler);
        rpcServer.start("10.140.42.170", 7777);

    }

}