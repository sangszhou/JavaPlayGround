package disJobFramework.core.client;

import disJobFramework.core.task.Task;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Boss {

    void submitTask(Task task);

    void retrieveTask(Task task);

    void submitJar(String host, int port, String filePath);
}
