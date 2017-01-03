package elasticsearch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.elasticsearch.action.*;
import org.elasticsearch.client.Client;

/**
 * Created by xinszhou on 15/12/2016.
 */
@Aspect
public class RequestInstrumentation {

    @Pointcut("execution(* org.elasticsearch.client.transport.support.InternalTransportClient.execute*(..)) && args(action, request, listener)")
    public <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder, Client>> void onExecutionListener(Action<Request, Response, RequestBuilder, Client> action, Request request, ActionListener<Response> listener) {
    }


    @Around("onExecutionListener(action, request, listener) ")
    public <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder, Client>> void aroundExecutionListener(ProceedingJoinPoint pjp, Action<Request, Response, RequestBuilder, Client> action, Request request, ActionListener<Response> listener) {
        try {
            System.out.println("    before es request...");
            pjp.proceed(new Object[]{action, request, listener});
            System.out.println("    after es request...");
        } catch (Throwable t) {
        }
    }
}
