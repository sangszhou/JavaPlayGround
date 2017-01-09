package oracle_nio_example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by xinszhou on 02/12/2016.
 * it works
 */
public class TimeQuery {
    static int DAYTIME_PORT = 8013;

    static ByteBuffer buffer = ByteBuffer.allocate(1024);

    static void query(String host) throws IOException {
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), DAYTIME_PORT);

        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.connect(isa);
            buffer.clear();
            sc.read(buffer);
            buffer.flip();

            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);

            String data = new String(bytes);

            System.out.println("time received from server is " + data);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        query(InetAddress.getLocalHost().getHostName());
    }

}
