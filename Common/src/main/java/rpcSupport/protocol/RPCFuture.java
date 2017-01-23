package rpcSupport.protocol;

import java.util.concurrent.*;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class RPCFuture implements Future<Object> {

    volatile RpcRequest request;
    volatile RpcResponse response;

    public RPCFuture(RpcRequest request) {
        this.request = request;
    }

    CountDownLatch latch = new CountDownLatch(1);

    public void complete(RpcResponse response) {
        synchronized (this) {
            if (this.response == null) {
                this.response = response;
                latch.countDown();
            }
        }
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

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        latch.await();
        return this.response;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);
        return this.response;
    }
}
