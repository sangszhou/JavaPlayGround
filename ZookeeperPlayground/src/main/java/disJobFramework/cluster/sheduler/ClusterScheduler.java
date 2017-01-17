package disJobFramework.cluster.sheduler;

import disJobFramework.core.client.Resource;
import disJobFramework.core.client.Worker;
import disJobFramework.core.scheduler.Scheduler;
import disJobFramework.core.task.Task;
import remote.netty.fileserver.HttpStaticFileServer;
import rpcSupport.server.RPCServer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ClusterScheduler implements Scheduler {

    List<Worker> restWorkers;
    InMemoryTaskQueue taskQueue;

    String uploadedFileDir = "";

    HttpStaticFileServer httpStaticFileServer;

    public ClusterScheduler() {
        restWorkers = new LinkedList<>();
        taskQueue = new InMemoryTaskQueue();
        httpStaticFileServer = new HttpStaticFileServer(5566);
    }

    @Override
    public void onTaskSubmitted(Task task) {
        System.out.println("scheduler receive one task with id: " + task.getTaskId());
        try {
            System.out.println("executing the task");
            task.call();
            taskQueue.putTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task onWorkerRetrieveTask(Resource resource) {
        Task task;

        while ((task = taskQueue.pollTask()) == null) {
            try {
                Thread.sleep(1000);
                System.out.println("there is a worker trying to poll job from scheduler");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return task;
    }

    public static void main(String args[]) {
        Scheduler clusterScheduler = new ClusterScheduler();
        RPCServer rpcServer = new RPCServer();

        // note this is scheduler's name, not self
        rpcServer.registerBean(Scheduler.class.getName(), clusterScheduler);

        rpcServer.start("10.140.42.170", 7777);
    }

}
