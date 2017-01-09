package NettyRPC.protocol;

/**
 * Created by xinszhou on 04/12/2016.
 * @todo add field ExpectingResult
 */
public class RpcRequest {

    String requestId;
    String className;
    String methodName;
    Class<?>[] parameterTypes;
    Object[] parameters;

    public String getRequestId() {
        return requestId;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
