package disJobFramework.cluster.taskes;

import disJobFramework.core.task.Task;
import disJobFramework.core.task.TaskState;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class HelloWorldTask implements Task {

    String taskId;

    public HelloWorldTask(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public TaskState getTaskState() {
        return TaskState.Running;
    }

    @Override
    public Object call() throws Exception {
        System.out.println("Hello world, this a task from boss being executed");
        return null;
    }
}
