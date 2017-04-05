package NettyRPC.rpcServer;

import NettyRPC.protocol.RpcRequest;
import NettyRPC.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> registeredService;

    public RpcHandler(Map<String, Object> services) {
        registeredService = services;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

        System.out.println("message received in RpcHandler");

        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        try {
            Object result = handle(rpcRequest);
            response.setResult(result);
        } catch (Throwable throwable) {
            System.out.println("throw exception when handling rpcRequest");
            throwable.printStackTrace();
            response.setError(throwable.getMessage());
        }

        channelHandlerContext.writeAndFlush(response).addListener(channelFuture ->
                System.out.println("response sent for request id: " + rpcRequest.getRequestId()));
    }

    public Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Object serviceBean = registeredService.get(className);
        Class<?> cls = serviceBean.getClass();

        // cglib reflection
        FastClass fastClass = FastClass.create(cls);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
        return fastMethod.invoke(serviceBean, parameters);

        // jdk reflection
//        Method method = cls.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        method.invoke(serviceBean, parameters);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        System.out.println("server exception caught");
        System.out.println(throwable.getMessage());
        ctx.close();
    }

}
