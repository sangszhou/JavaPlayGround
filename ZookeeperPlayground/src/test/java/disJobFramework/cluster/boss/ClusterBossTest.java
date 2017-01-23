package disJobFramework.cluster.boss;

import disJobFramework.cluster.taskes.ComplexTask;
import disJobFramework.cluster.taskes.HelloWorldTask;
import disJobFramework.core.client.Boss;
import disJobFramework.core.task.Task;
import org.junit.Test;
import rpcSupport.transport.ConnectionManager;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 1/17/17.
 */
public class ClusterBossTest {
    String schedulerHost = "10.140.42.170";
    int schedulerPort = 7777;
    int jarPort = 5566;
    String jarFilePath = "/ws/github/JavaPlayGround/ZookeeperPlayground/src/main/java/disJobFramework/jarsStorage/bossJars/ZookeeperPlayground-1.0-SNAPSHOT-jar-with-dependencies.jar";


    @Test
    public void uploadFile2Server() {
        Boss boss = new ClusterBoss(null);
        boss.submitJar(schedulerHost, jarPort, jarFilePath);
    }

    @Test
    public void sendClass2Server() {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connectServer(new InetSocketAddress(schedulerHost, schedulerPort));

        Task simpleTask = new HelloWorldTask("1");

        Boss boss = new ClusterBoss(connectionManager);
        boss.submitTask(simpleTask);
    }

    // classes with class definition that are not exist on other machine
    // send with class and jar
    @Test
    public void sendJarAndTaskClient() {
        //send jar to scheduler
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connectServer(new InetSocketAddress(schedulerHost, schedulerPort));
        Boss boss = new ClusterBoss(connectionManager);

        System.out.println("before send time: " + System.currentTimeMillis()/1000);
//        boss.submitJar(schedulerHost, jarPort, jarFilePath);
        System.out.println("after send time: " + System.currentTimeMillis()/1000);

        Task simpleTask = new HelloWorldTask("[hello world task]");
        boss.submitTask(simpleTask);
    }

}