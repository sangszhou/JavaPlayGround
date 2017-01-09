package NettyRPC.rpcClient;

import NettyRPC.protocol.RpcRequest;
import org.omg.CORBA.Object;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class RequestQueue {
    ConcurrentHashMap<String, Future<Object>> inFlightRequest;

    public RequestQueue(ConcurrentHashMap<String, Future<Object>> inFlightRequest) {
        this.inFlightRequest = inFlightRequest;
    }

    public void complete(String requestId) {
        if (inFlightRequest.get(requestId) == null) {
            System.out.println("Failed to find request in inFlightRequests");
        } else {
            inFlightRequest.get(requestId);
        }
    }

}
