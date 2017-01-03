package aspectjaop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by xinszhou on 20/12/2016.
 */

@Aspect
public class TraceAspect {
    @Pointcut("@annotation(Trace)")
    public void tracePointcutDefinition() {}

//    @Around("@annotation(Trace) && execution(* *(..))")
    @Around("@annotation(Trace) && execution(* *(..))")
    public Object aroundTrace(ProceedingJoinPoint pjp) throws Throwable {
        Object returnObj = null;

        try {
            System.out.println("before traced method started...");
            returnObj = pjp.proceed();

        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            System.out.println("after traced method executed...");
        }

        return returnObj;
    }

}
