package kafka_style_nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class KafkaChannel {

    String id;
    TransportLayer transportLayer;
    NetworkReceive receive;
    Send send;
    int maxReceiveSize;

    public KafkaChannel(String id, TransportLayer transportLayer, int maxReceiveSize) {
        this.id = id;
        this.transportLayer = transportLayer;
        this.maxReceiveSize = maxReceiveSize;
    }

    public void close() throws IOException {
        transportLayer.close();
    }

    public void disconnect() {
        transportLayer.disconnect();
    }

    public boolean isConnected() {
        return transportLayer.isConnected();
    }

    public boolean hasSend() {
        return send != null;
    }

    public InetAddress socketAddress() {
        return transportLayer.socketChannel().socket().getInetAddress();
    }

    public void setSend(Send send) {
        if (this.send != null) {
            throw new IllegalStateException("Attempt to begin a send operation with prior send operation still in progress.");
        }
        this.send = send;
        this.transportLayer.addInterestOps(SelectionKey.OP_WRITE);
    }

    // how to ensure that read is called once for one result
    public NetworkReceive read() throws IOException {
        NetworkReceive result = null;
        if (receive == null) {
            receive = new NetworkReceive(maxReceiveSize, id);
        }

        receive.readFrom(transportLayer.socketChannel());
        if(receive.complete()) {
            receive.payload().rewind();
            result = receive;
            receive = null;
        }
        return result;
    }

    public Send write() throws IOException {
        Send result = null;
        if (send != null) {
            send.writeTo(transportLayer.socketChannel());
            if (send.completed()) {
                transportLayer.removeInterestOps(SelectionKey.OP_WRITE);
            }
        }
        if (send.completed()) {
            result = send;
        }
        return result;
    }

}
