package raw.kafka;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by xinszhou on 30/12/2016.
 */
// not thread safe
public class BlockingChannel {

    String host;
    int port;
    int readBufferSize;
    int writeBufferSize;
    int readTimeoutMs;

    public BlockingChannel(String host, int port, int readBufferSize, int writeBufferSize, int readTimeoutMs) {
        this.host = host;
        this.port = port;
        this.readBufferSize = readBufferSize;
        this.writeBufferSize = writeBufferSize;
        this.readTimeoutMs = readTimeoutMs;
    }

    boolean connected = false;
    SocketChannel channel = null;
    ReadableByteChannel readChannel = null;
    GatheringByteChannel writeChannel = null;
    Object lock = new Object();
    int connectTimeout = readTimeoutMs;
    String connectionId = null;

    {
        // when this code is initialized?
    }

    public void connect() {
        synchronized (lock) {
            if (!connected) {
                try {
                    channel = SocketChannel.open();
                    if (readBufferSize > 0)
                        channel.socket().setReceiveBufferSize(readBufferSize);
                    if (writeBufferSize > 0)
                        channel.socket().setSendBufferSize(writeBufferSize);
                    //@// TODO: 30/12/2016 understand what those conf means
                    channel.configureBlocking(true);
                    channel.socket().setSoTimeout(readTimeoutMs);
                    channel.socket().setTcpNoDelay(true);
                    channel.socket().connect(new InetSocketAddress(host, port), connectTimeout);

                    writeChannel = channel;

                    // Need to create a new ReadableByteChannel from input stream because SocketChannel
                    // doesn't implement read with timeout
                    // See: http://stackoverflow.com/questions/2866557/timeout-for-socketchannel-doesnt-work
                    readChannel = Channels.newChannel(channel.socket().getInputStream());
                    connected = true;

                    String localHost = channel.socket().getLocalAddress().getHostAddress();
                    int localPort = channel.socket().getLocalPort();
                    String remoteHost = channel.socket().getInetAddress().getHostAddress();
                    int remotePort = channel.socket().getPort();

                    connectionId = localHost + ":" + localPort + "-" + remoteHost + ":" + remotePort;

                } catch (IOException e) {
                    e.printStackTrace();
                    disconnect();
                }

            }
        }
    }

    public void disconnect() {
        synchronized (lock) {
            if (channel != null) {
                try {
                    channel.close();
                    channel.socket().close();
                    channel = null;
                    writeChannel = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (readChannel != null) {
                try {
                    readChannel.close();
                    readChannel = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    // read, write 操作都是对 ByteBuffer[]

}
