package disJobFramework.core.client;

import disJobFramework.core.task.Task;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Worker {

    String getHost();

    int getPort();

    void executeTask(Task task);

    boolean fetchTask();

}
