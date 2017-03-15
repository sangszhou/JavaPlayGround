package v_0_10_0;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * 这个例子还不是太好， 更好的应该是模拟出从数据库读写的样子
 * public class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {
 * public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
 * commitDBTransaction();
 * }
 * <p>
 * public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
 * for(TopicPartition partition: partitions)
 * consumer.seek(partition, getOffsetFromDB(partition));
 * }
 * } }
 */

public class ConsumerThreadWithSeek implements Runnable {
    private String topicName;
    private String groupId;
    private long startingOffset;
    private KafkaConsumer<String, String> kafkaConsumer;

    public ConsumerThreadWithSeek(String topicName, String groupId, long startingOffset) {
        this.topicName = topicName;
        this.groupId = groupId;
        this.startingOffset = startingOffset;
    }

    public void run() {
        Properties configProperties = new Properties();
        configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "offset123");
        configProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        //Figure out where to start processing messages from
        kafkaConsumer = new KafkaConsumer<>(configProperties);

        kafkaConsumer.subscribe(Arrays.asList(topicName), new ConsumerRebalanceListener() {
            // 有其他 client 加入， 此 client 收到的部分消息转到新的 client
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
            }

            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.printf("%s topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));

                if (startingOffset == 0) {
                    System.out.println("Setting offset to beginning");
                    kafkaConsumer.seekToBeginning(partitions);
                    return;
                } else if (startingOffset == -1) {
                    System.out.println("Setting it to the end ");
                    kafkaConsumer.seekToEnd(partitions);
                    return;
                }

                Iterator<TopicPartition> topicPartitionIterator = partitions.iterator();
                while (topicPartitionIterator.hasNext()) {
                    TopicPartition topicPartition = topicPartitionIterator.next();

                    System.out.println("Current offset is " + kafkaConsumer.position(topicPartition)
                            + " committed offset is ->" + kafkaConsumer.committed(topicPartition));
                    System.out.println("Resetting offset to " + startingOffset);

                    kafkaConsumer.seek(topicPartition, startingOffset);
                }
            }
        });

        // Start processing messages
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.value());
                }
            }
        } catch (WakeupException ex) {
            System.out.println("Exception caught " + ex.getMessage());
        } finally {
            kafkaConsumer.close();
            System.out.println("After closing KafkaConsumer");
        }
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return this.kafkaConsumer;
    }
}

