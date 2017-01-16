package disJobFramework.core.task;


import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Task<V> extends Callable<V>, Serializable {

    String getTaskId();

    TaskState getTaskState();

}
