package base.property.common;

/**
 * Created by xinszhou on 04/04/2017.
 */
public class Scheduler {
    public static void tick() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
