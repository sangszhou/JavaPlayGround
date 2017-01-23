package rpcSupport.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.protocol.RpcResponse;

import java.util.Map;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ServerRpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> registeredServices;

    public ServerRpcHandler(Map<String, Object> registeredServices) {
        this.registeredServices = registeredServices;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        System.out.println("client connected to server");
    }


    // @todo delegate the operation to another thread
    private Object handle(RpcRequest request) throws Exception {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // will throw exception if no matching service
        // cglib reflection
        Object serviceBean = registeredServices.get(className);
        Class<?> cls = serviceBean.getClass();

        System.out.println("mark123: " + parameters[0].getClass().getName());
        // jdk reflection
//        Method method = cls.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        method.invoke(serviceBean, parameters);

        FastClass fastClass = FastClass.create(cls);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
        return fastMethod.invoke(serviceBean, parameters);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        System.out.println("server received message from client with requestId: " + msg.getRequestId());

        RpcResponse response = new RpcResponse();
        response.setRequestId(msg.getRequestId());
        response.setVersionNo(msg.getVersionNo());

        try {
            Object result = handle(msg);
            response.setResult(result);
            response.setStatus("success");
        } catch (Exception exp) {
            response.setMessage(exp.getMessage());
            response.setStatus("fail");
        }

        ctx.writeAndFlush(response).addListener(channelFuture ->
                System.out.println("response sent for request id: " + msg.getRequestId()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
    }

}
