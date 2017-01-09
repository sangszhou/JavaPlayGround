package oracle_nio_example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class TimeServer {
    static int port = 8013;

    static ByteBuffer buffer = ByteBuffer.allocate(1024);

    static ServerSocketChannel setup() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), port);
        ssc.socket().bind(isa);
        return ssc;
    }

    static void server(ServerSocketChannel ssc) throws IOException {
        SocketChannel sc = ssc.accept();

        try {
            String now = new Date().toString();
            buffer.clear();
            buffer.put(now.getBytes());
            buffer.flip();
            sc.write(buffer);
            System.out.println(sc.socket().getInetAddress() + " : " + now);
            sc.close();
        } finally {
            sc.close();
        }
    }

    public static void main(String args[]) throws Exception {
        ServerSocketChannel ssc = setup();

        for(;;) {
            server(ssc);
        }

    }

}
