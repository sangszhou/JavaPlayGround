package student;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by xinszhou on 15/12/2016.
 */
public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("student.xml");

        Student student = (Student) context.getBean("student");

        student.getName();
        student.getAge();

//        student.printThrowException();
    }

}
