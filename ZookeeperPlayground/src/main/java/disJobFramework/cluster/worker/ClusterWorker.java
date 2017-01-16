package disJobFramework.cluster.worker;

import disJobFramework.cluster.boss.ClusterBoss;
import disJobFramework.cluster.taskes.HelloWorldTask;
import disJobFramework.core.client.Resource;
import disJobFramework.core.client.Worker;
import disJobFramework.core.scheduler.Scheduler;
import disJobFramework.core.task.Task;
import rpcSupport.client.RpcClientHandler;
import rpcSupport.protocol.RPCFuture;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.protocol.RpcResponse;
import rpcSupport.transport.ConnectionManager;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ClusterWorker implements Worker {

    static AtomicLong requestId = new AtomicLong(0);

    ConnectionManager connectionManager;

    public ClusterWorker(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void executeTask(Task task) {
        try {
            task.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean fetchTask() {
        RpcClientHandler handler;

        while ((handler = connectionManager.getHandler()) == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        RPCFuture rpcFuture = handler.sendRequest(ClusterBoss.task2RpcRequest(new DummyResource(1)));
        RPCFuture rpcFuture = handler.sendRequest(resource2RpcRequest(new DummyResource(1)));

        try {
            RpcResponse response = (RpcResponse) rpcFuture.get();
            Task t = (Task) response.getResult();
            System.out.println("executing task on worker");
            executeTask(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static RpcRequest resource2RpcRequest(Resource resource) {
        RpcRequest request = new RpcRequest();

        request.setRequestId(requestId.getAndIncrement());
        request.setVersionNo(0);
        request.setClassName(Scheduler.class.getName());

        request.setMethodName("onWorkerRetrieveTask");
        request.setParameterTypes(new Class[]{Resource.class});
        request.setParameters(new Object[]{resource});

        return request;
    }

    public static void main(String args[]) {
        String host = "localhost";
        int port = 7777;
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connectServer(new InetSocketAddress(host, port));

        ClusterWorker worker = new ClusterWorker(connectionManager);
        worker.fetchTask();
    }


}
