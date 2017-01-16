package disJobFramework.cluster.sheduler;

import disJobFramework.core.scheduler.TaskQueue;
import javafx.concurrent.Task;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by xinszhou on 1/14/17.
 */
public class InMemoryQueue implements TaskQueue {

    Deque<Task> taskQueue = new LinkedList<>();

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
