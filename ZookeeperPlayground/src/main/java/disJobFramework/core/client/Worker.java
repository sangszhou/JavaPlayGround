package disJobFramework.core.client;

import javafx.concurrent.Task;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Worker {

    void executeTask(Task task);

    boolean connect(String host, int port);

}
