package nio_socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xinszhou on 02/12/2016.
 *
 * Not working...
 */
public class MultiPortEchoServer {
    private int []ports;
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public MultiPortEchoServer(int[] ports) throws IOException {
        this.ports = ports;
        go();
    }

    void go() throws IOException {
        Selector selector = Selector.open();

        for(int i = 0; i < ports.length; i ++) {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);

            SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Listening to port " + ports[i]);
        }

        while (true) {
            int num = selector.select();

            if(num > 0) {
                System.out.println("keys are attached to server: " + num);
            }

            Set selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();
            while (it.hasNext()) {

                SelectionKey key = (SelectionKey) it.next();
                it.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel ssc =(ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
                    System.out.println("Got connection from " + sc);
                } else if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();

                    int bytesEncoded = 0;
                    while (true) {
                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r <= 0) {
                            break;
                        }
                        buffer.flip();
                        sc.write(buffer);
                        bytesEncoded += r;
                    }
                    System.out.println("Echoed: " + bytesEncoded + " socket channel info: "+ sc);
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        int[] ports = new int[1];
        ports[0] = 8899;

        new MultiPortEchoServer(ports);
    }

}
