package disJobFramework.core.client;

import javafx.concurrent.Task;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Boss {

    void submitTask(Task task);

    void retrieveTask(Task task);

    void connect(String host, int port);
}
