package kafka_style_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by xinszhou on 02/12/2016.
 */
public interface Selectable {

    // client 端的使用方式
    public void connect(String id, InetSocketAddress address, int sendBufferSize, int receiveBufferSize) throws Exception;

    public void wakeup();

    public void close();

    public void close(String id);

    public void send(Send send);

    public void poll(long timeout) throws IOException;

    /**
     * The list of sends that completed on the last {@link #poll(long) poll()} call.
     */
    public List<Send> completedSends();

    /**
     * The list of receives that completed on the last {@link #poll(long) poll()} call.
     * 用途有些模糊
     */
    public List<NetworkReceive> completedReceives();

    /**
     * The list of connections that finished disconnecting on the last {@link #poll(long) poll()}
     * call.
     */
    public List<String> disconnected();

    /**
     * The list of connections that completed their connection on the last {@link #poll(long) poll()}
     * call.
     */
    public List<String> connected();

    /**
     * returns true  if a channel is ready
     * @param id The id for the connection
     */
    public boolean isChannelReady(String id);


}
