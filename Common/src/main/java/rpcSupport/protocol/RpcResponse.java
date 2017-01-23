package rpcSupport.protocol;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class RpcResponse {
    long requestId;
    long versionNo;

    Object result;
    String status;
    String message;

    public long getRequestId() {
        return requestId;
    }

    public long getVersionNo() {
        return versionNo;
    }

    public Object getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setVersionNo(long versionNo) {
        this.versionNo = versionNo;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
