package disJobFramework.core.task;

/**
 * Created by xinszhou on 1/14/17.
 */
public enum TaskState {

    /**
     * all tasks start in the PENDING state. This state corresponds to the period of time when
     * the scheduler is trying to acquire resources for the task but has not yet succeeded.
     * Task enters PENDING state when it gets in the task queue, and remains in this state
     * until all its required resources ownerships are acquired and the scheduler has
     * informed the API service to dispatch the task.
     */

    Pending, Running, Done, Released;
}
