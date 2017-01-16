package disJobFramework.cluster.sheduler;

import disJobFramework.core.scheduler.TaskQueue;
import disJobFramework.core.task.Task;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by xinszhou on 1/14/17.
 */
public class InMemoryTaskQueue implements TaskQueue {

    Deque<Task> taskQueue = new ConcurrentLinkedDeque<>();

    @Override
    public void init() {
        // load all the tasks from the file system
    }

    @Override
    public Task pollTask() {
        return taskQueue.poll();

    }

    public void putTask(Task task) {
        taskQueue.add(task);
    }
}
