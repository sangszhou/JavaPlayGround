package NettyRPC.protocol;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class RpcResponse {

    String requestId;
    Object result;
    String error;

    public String getRequestId() {
        return requestId;
    }

    public Object getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }
}
