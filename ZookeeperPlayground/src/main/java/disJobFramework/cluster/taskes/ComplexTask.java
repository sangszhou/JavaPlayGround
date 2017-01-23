//package disJobFramework.cluster.taskes;
//
//import disJobFramework.core.task.Task;
//import disJobFramework.core.task.TaskState;
//
///**
// * Created by xinszhou on 17/01/2017.
// */
//public class ComplexTask implements Task {
//
//    String jarLocation;
//
//    Cat cat = new Cat();
//
//    @Override
//    public String getTaskId() {
//        return "cat-task";
//    }
//
//    @Override
//    public void setJarLocation(String jarLocation) {
//        this.jarLocation = jarLocation;
//    }
//
//    @Override
//    public String getJarLocation() {
//        return jarLocation;
//    }
//
//    @Override
//    public TaskState getTaskState() {
//        return TaskState.Done;
//    }
//
//    @Override
//    public Object call() throws Exception {
//        cat.play();
//        return null;
//    }
//}
