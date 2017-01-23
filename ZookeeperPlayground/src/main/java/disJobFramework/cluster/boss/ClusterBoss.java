package disJobFramework.cluster.boss;

import disJobFramework.cluster.taskes.ComplexTask;
import disJobFramework.core.client.Boss;
import disJobFramework.core.scheduler.Scheduler;
import disJobFramework.core.task.Task;
import remote.netty.fileserver.FileUploadClient;
import rpcSupport.client.RpcClientHandler;
import rpcSupport.protocol.RPCFuture;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.transport.ConnectionManager;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ClusterBoss implements Boss {

    static AtomicLong requestId = new AtomicLong(0);

    ConnectionManager connectionManager;

    public ClusterBoss(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void submitJar(String host, int port, String filePath) {
        try {
            FileUploadClient.uploadFile(host, port, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submitTask(Task task) {
        RpcRequest request = task2RpcRequest(task);

        RpcClientHandler handler;

        while ((handler = connectionManager.getHandler()) == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RPCFuture rpcFuture = handler.sendRequest(request);

        try {
            rpcFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void retrieveTask(Task task) {
        System.out.println("No implemented yet");
    }

    public static RpcRequest task2RpcRequest(Task t) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(requestId.getAndIncrement());
        rpcRequest.setVersionNo(0);

        // will this class's child still works
        rpcRequest.setClassName(Scheduler.class.getName());
        rpcRequest.setMethodName("onTaskSubmitted");

        rpcRequest.setParameterTypes(new Class[]{Task.class});
        rpcRequest.setParameters(new Object[]{t});

        return rpcRequest;
    }


    public static void main(String args[]) {
        String schedulerHost = "10.140.42.170";
        int schedulerPort = 7777;
        int jarPort = 5566;

        String jarFilePath = "/ws/github/JavaPlayGround/ZookeeperPlayground/src/main/java/disJobFramework/jarsStorage/bossJars/ZookeeperPlayground-1.0-SNAPSHOT-jar-with-dependencies.jar";

        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connectServer(new InetSocketAddress(schedulerHost, schedulerPort));

//      Task simpleTask = new HelloWorldTask("1");

//        Task simpleTask = new ComplexTask();
        Boss boss = new ClusterBoss(connectionManager);
//      boss.submitTask(simpleTask);


        boss.submitJar(schedulerHost, jarPort, jarFilePath);

    }

}
