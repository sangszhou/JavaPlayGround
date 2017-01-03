package aspectjaop.annotation;

/**
 * Created by xinszhou on 20/12/2016.
 */
public class TraceTest {
    public static void main(String args[]) {
        TraceTest test = new TraceTest();
        test.rpcCall();
    }

    // 虽然 intellij 没有给出提示，但是这个 Trace 还是成功的
    @Trace
    public void rpcCall() {
        System.out.println("call rpc");
    }

}
