package firstTry.core.networking.rpc;

/**
 * Created by xinszhou on 09/01/2017.
 */
public class RPCResponse {
    String requestId;
    Object result;
    boolean isSuccess;
    String error;

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequestId() {
        return requestId;
    }

    public Object getResult() {
        return result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getError() {
        return error;
    }
}
