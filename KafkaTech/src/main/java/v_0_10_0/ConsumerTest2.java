package v_0_10_0;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.*;

/**
 * Created by xinszhou on 13/02/2017.
 */
public class ConsumerTest2 {
    public static Map<String, List<KafkaStream<byte[], byte[]>>> createConsumer(String zookeeperConnect,
                                                                                String consumerGroup,
                                                                                String sessionTimeout,
                                                                                String syncTime,
                                                                                String commitInterval,
                                                                                String connectionTimeout,
                                                                                String consumerTimeout,
                                                                                String autoOffsetReset,
                                                                                String[] topics) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperConnect);
        props.put("group.id", consumerGroup);
        props.put("zookeeper.session.timeout.ms", sessionTimeout);
        props.put("zookeeper.sync.time.ms", syncTime);
        props.put("auto.commit.interval.ms", commitInterval);
        props.put("zookeeper.connection.timeout.ms", connectionTimeout);
        props.put("consumer.timeout.ms", consumerTimeout);
        props.put("auto.offset.reset", autoOffsetReset);

        ConsumerConfig config = new ConsumerConfig(props);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

        Arrays.asList(topics).forEach(topic -> topicCountMap.put(topic, 1));

        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);

        // Create a list of MessageStreams of type T for each topic
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);

//        consumerMap.get(topics[0]).forEach(stream -> {
//            ConsumerIterator<byte[], byte[]> it = stream.iterator();
//            // hasNext usually blocking
//            while (it.hasNext()) {
//            }
//            System.out.println("Shutting down Thread: ");
//        });

        return consumerMap;
    }

    public static void main(String args[]) {


        String zookeepers = "localhost:32782";
        String consumerGroup = "dsfdg33fdg";
        String sessionTimeout = "10000";
        String syncTime = "300";
        String commitInterval = "300";
        String connectionTimeout = "10000";
        String consumerTimeout = "210000";
        String autoOffsetReset = "smallest";
        String topics[] = new String[]{"test", "page_visits"};

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = ConsumerTest2.createConsumer(zookeepers,
                consumerGroup, sessionTimeout,
                syncTime, commitInterval, connectionTimeout,
                consumerTimeout, autoOffsetReset, topics);

        // what's the first topic?
        System.out.println("the first topic is " + topics[0]);

//        ConsumerIterator<byte[], byte[]> ite = consumerMap.get(topics[0]).get(0).iterator();

        Map<String, ConsumerIterator<byte[], byte[]>> consumerIterator;

        consumerIterator = new HashMap<>();

        for (Map.Entry<String, List<KafkaStream<byte[], byte[]>>> entry : consumerMap.entrySet()) {
            consumerIterator.put(entry.getKey(), entry.getValue().get(0).iterator());
        }

        while (consumerIterator.get("test").hasNext()) {

            MessageAndMetadata<byte[], byte[]> msg = consumerIterator.get("test").next();

            if (msg.message() != null) {
                String msgVal = new String(msg.message());
                System.out.println("value is " + msgVal);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}
