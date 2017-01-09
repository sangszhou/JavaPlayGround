package nio_socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class UseScatterGatherServer {

    static final int FirstHeaderLength = 2;
    static final int SecondHeaderLength = 4;
    static final int BodyLength = 6;

    public static void main(String args[]) throws IOException {
        int port = 3233;

        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(port);
        ssc.socket().bind(address);

        int messageLength = FirstHeaderLength + SecondHeaderLength + BodyLength;
        ByteBuffer[] buffers = new ByteBuffer[3];

        buffers[0] = ByteBuffer.allocate(FirstHeaderLength);
        buffers[1] = ByteBuffer.allocate(SecondHeaderLength);
        buffers[2] = ByteBuffer.allocate(BodyLength);

        SocketChannel sc = ssc.accept();

        while (true) {
            int bytesRead = 0;
            while (bytesRead < messageLength) {
                long r = sc.read(buffers);
                bytesRead += r;
                System.out.println("r " + r);

                for (int i = 0; i < buffers.length; i++) {
                    ByteBuffer bb = buffers[i];
                    System.out.println("b " + i + " " + bb.position() + " " + bb.limit());
                }
            }

            for (int i = 0; i < buffers.length; i++) {
                ByteBuffer bb = buffers[i];
                bb.flip();
            }

            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                long r = sc.write(buffers);
                bytesWritten += r;
            }

            // Clear buffers
            for (int i = 0; i < buffers.length; ++i) {
                ByteBuffer bb = buffers[i];
                bb.clear();
            }

            System.out.println(bytesRead + " " + bytesWritten + " " + messageLength);
        }
    }
}
