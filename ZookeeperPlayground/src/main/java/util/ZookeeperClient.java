package util;

/**
 * Created by xinszhou on 30/12/2016.
 */
public class ZookeeperClient {

    public class ZookeeperConnectionException extends Exception {
        public ZookeeperConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
