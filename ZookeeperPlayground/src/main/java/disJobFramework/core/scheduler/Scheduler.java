package disJobFramework.core.scheduler;

import disJobFramework.core.client.Worker;
import disJobFramework.core.task.Task;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Scheduler {

    // if this method needed?
    void onWorkerConnected(Worker worker);

    void onTaskSubmitted(Task task);

    void onWorkerRetrieveTask(Worker worker);

}
