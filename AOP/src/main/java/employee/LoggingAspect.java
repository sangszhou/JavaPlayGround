package employee;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by xinszhou on 15/12/2016.
 */
@Aspect
public class LoggingAspect {

    @Before("execution(* employee.EmployeeManagerImpl.*(..))") // expression
    public void logBeforeAllMethods(JoinPoint jp) {
        System.out.println("****LoggingAspect.logBeforeAllMethods() " + jp.getSignature().getName());
    }
}
