package ali_middleware_contest.rpcs.server;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class EchoServer {
    public String echo(String incomingMsg) {
        return "confirmed: " + incomingMsg;
    }
}
