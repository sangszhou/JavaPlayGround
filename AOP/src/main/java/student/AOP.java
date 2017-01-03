package student;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by xinszhou on 15/12/2016.
 */
@Aspect
public class AOP {

    @Pointcut("execution(* student.Student.getName(..))")
    private void getname() {
    }

    @Before("getname()")
    public void doBeforeTask() {
        System.out.println("    before getName() called...");
    }

    @After("getname()")
    public void doAfterTask() {
        System.out.println("    after getName() called...");
    }
}
