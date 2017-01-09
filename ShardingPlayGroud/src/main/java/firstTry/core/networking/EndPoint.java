package firstTry.core.networking;

/**
 * Created by xinszhou on 06/01/2017.
 */
public class EndPoint {

    String host;
    String port;

    public EndPoint(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}
