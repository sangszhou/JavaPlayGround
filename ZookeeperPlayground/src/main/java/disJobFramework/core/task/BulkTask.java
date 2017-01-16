package disJobFramework.core.task;


import java.util.List;

/**
 * Created by xinszhou on 1/14/17.
 */
public abstract class BulkTask<V> implements Task<V> {

    public abstract List<Task<V>> getTasks();

    public abstract void setTask(List<Task<V>> tasks);
}
