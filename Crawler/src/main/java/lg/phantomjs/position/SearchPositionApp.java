package lg.phantomjs.position;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by xinszhou on 14/01/2017.
 */
public class SearchPositionApp {
    public static void main(String args[]) {
        ApplicationContext context = new AnnotationConfigApplicationContext("lg.phantomjs.position", "util");
        SearchPositionScheduler scheduler = context.getBean(SearchPositionScheduler.class);

        scheduler.searchPosition();

    }
}
