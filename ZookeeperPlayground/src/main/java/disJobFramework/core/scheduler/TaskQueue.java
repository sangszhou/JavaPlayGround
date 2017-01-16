package disJobFramework.core.scheduler;


import disJobFramework.core.task.Task;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface TaskQueue {

    void init();

    Task pollTask();

    void putTask(Task task);
}

