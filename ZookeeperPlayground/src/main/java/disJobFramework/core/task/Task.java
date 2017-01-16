package disJobFramework.core.task;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.concurrent.Callable;

/**
 * Created by xinszhou on 1/14/17.
 */
public interface Task<V> extends Callable<V> {

    String getTaskId();

    TaskState getTaskState();


}
