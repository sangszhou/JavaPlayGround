package nio_socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class MultiPortEchoClient {

    public static void main(String args[]) throws Exception {
        String HOST = "127.0.0.1";
        int port = 8899;

        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        boolean connected = sc.connect(new InetSocketAddress(HOST, port));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(100);

        if (connected) {
            int w = sc.write(buffer);

            System.out.println("write bytes to server: " + w);
        }

        Thread.sleep(3000);

        sc.finishConnect();
        sc.close();

    }

}
