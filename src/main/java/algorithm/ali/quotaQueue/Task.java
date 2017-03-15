package algorithm.ali.quotaQueue;

/**
 * Created by xinszhou on 23/02/2017.
 */
public class Task {

    String name;

    public void run() {
        System.out.println("executing task: " + name);
    }

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
