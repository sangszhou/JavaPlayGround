package kafka_style_nio;

import java.nio.channels.SelectionKey;

/**
 * Created by xinszhou on 02/12/2016.
 */
public interface ChannelBuilder {
    KafkaChannel buildChannel(String id, SelectionKey key, int maxReceiveSize) throws Exception;
    void close();
}
