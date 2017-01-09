package kafka_style_nio;

import io.netty.channel.ChannelOutboundBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class Selector implements Selectable {

    java.nio.channels.Selector jSelector;
    Map<String, KafkaChannel> channels;
    Map<KafkaChannel, Deque<NetworkReceive>> stagedReceives;
    List<String> connected;
    List<String> disconnected;
    List<String> failedSends;
    long connectionMaxIdNanos;
    int maxReceiveSize;
    ChannelBuilder channelBuilder;

    // 为什么构造函数不能抛出异常
    public Selector(long connectionMaxIdNanos, int maxReceiveSize, ChannelBuilder channelBuilder) {
        this.connectionMaxIdNanos = connectionMaxIdNanos;
        this.maxReceiveSize = maxReceiveSize;
        this.channelBuilder = channelBuilder;

        try {
            this.jSelector = java.nio.channels.Selector.open();
        } catch (Exception e) {

        } finally {

        }

    }

    @Override
    public void connect(String id, InetSocketAddress address, int sendBufferSize, int receiveBufferSize) throws Exception {

    }

    @Override
    public void wakeup() {

    }

    @Override
    public void close() {

    }

    @Override
    public void close(String id) {

    }

    @Override
    public void send(Send send) {

    }

    @Override
    public void poll(long timeout) throws IOException {

    }

    @Override
    public List<Send> completedSends() {
        return null;
    }

    @Override
    public List<NetworkReceive> completedReceives() {
        return null;
    }

    @Override
    public List<String> disconnected() {
        return null;
    }

    @Override
    public List<String> connected() {
        return null;
    }

    @Override
    public boolean isChannelReady(String id) {
        return false;
    }
}
