package NettyRPC.rpcClient;

import NettyRPC.protocol.RpcRequest;
import NettyRPC.protocol.RpcResponse;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class RPCFuture implements Future<Object> {
    volatile RpcRequest request;
    volatile RpcResponse response = null;

    CountDownLatch latch = new CountDownLatch(1);

    public RPCFuture(RpcRequest request) {
        this.request = request;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    public void complete(RpcResponse response) {
        if (this.response != null) {
            return;
        }
        this.response = response;
        latch.countDown();

    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        latch.await();

        if(response == null) {
            System.out.println("Should not happen");
            return response;
        }

        return response.getResult();

    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);

        if(latch.getCount() == 0) {
            return response.getResult();
        } else {
            return null;
        }
    }
}
