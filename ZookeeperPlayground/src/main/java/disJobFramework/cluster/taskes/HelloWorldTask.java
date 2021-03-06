package disJobFramework.cluster.taskes;

import disJobFramework.core.task.Task;
import disJobFramework.core.task.TaskState;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class HelloWorldTask implements Task {

    String taskId;
    String jarLocation;


    public HelloWorldTask() {

    }

    public HelloWorldTask(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setJarLocation(String jarLocation) {
        this.jarLocation = jarLocation;
    }

    @Override
    public String getJarLocation() {
        return jarLocation;
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
