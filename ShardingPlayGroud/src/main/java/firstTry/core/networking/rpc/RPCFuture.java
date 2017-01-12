package firstTry.core.networking.rpc;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by xinszhou on 09/01/2017.
 * todo, convert error message to Exception if failed to handle the request
 */
public class RPCFuture implements Future<Object> {

    RPCRequest request;
    RPCResponse response;

    CountDownLatch latch = new CountDownLatch(1);

    public RPCFuture(RPCRequest request) {
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

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);

        if (latch.getCount() == 0) {
            return response;
        }

        return null;
    }
}
