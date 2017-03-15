package v_0_10_0;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Created by xinszhou on 13/02/2017.
 */
public class ManualOffsetControl {

    /**
     * Each record comes with its own offset, so to manage your own offset you just need to do the following:

     1. Configure enable.auto.commit=false
     2. Use the offset provided with each ConsumerRecord to save your position.
     3. On restart restore the position of the consumer using seek(TopicPartition, long).

     Here are a couple of examples of this type of usage:

     1. If the results of the consumption are being stored in a relational database, storing the offset in the
     database as well can allow committing both the results and offset in a single transaction. Thus either the
     transaction will succeed and the offset will be updated based on what was consumed or the result will not be stored
     and the offset won't be updated.
     2. If the results are being stored in a local store it may be possible to store the offset there as well. For example
     a search index could be built by subscribing to a particular partition and storing both the offset and the indexed
     data together. If this is done in a way that is atomic, it is often possible to have it be the case that even if a
     crash occurs that causes unsync'd data to be lost, whatever is left has the corresponding offset stored as well.
     This means that in this case the indexing process that comes back having lost recent updates just resumes indexing
     from what it has ensuring that no updates are lost.

     Kafka allows specifying the position using seek(TopicPartition, long) to specify the new position. Special methods
     for seeking to the earliest and latest offset the server maintains are also available ( seekToBeginning(Collection)
     and seekToEnd(Collection) respectively).

     */

    public void storeOffsetOutsideKafka(KafkaConsumer<String, String> consumer) {

    }

    /**
     * In the previous examples, we subscribed to the topics we were interested in and let Kafka dynamically assign a
     * fair share of the partitions for those topics based on the active consumers in the group. However, in some cases
     * you may need finer control over the specific partitions that are assigned. For example:

     * 1. If the process is maintaining some kind of local state associated with that partition (like a local
     * on-disk key-value store), then it should only get records for the partition it is maintaining on disk.

     2. If the process itself is highly available and will be restarted if it fails (perhaps using a cluster
     management framework like YARN, Mesos, or AWS facilities, or as part of a stream processing framework). In this
     case there is no need for Kafka to detect the failure and reassign the partition since the consuming process will
     be restarted on another machine.

     Once assigned, you can call poll in a loop, just as in the preceding examples to consume records. The group that the consumer specifies is still used for committing offsets, but now the set of partitions will only change with another call to assign. Manual partition assignment does not use group coordination, so consumer failures will not cause assigned partitions to be rebalanced. Each consumer acts independently even if it shares a groupId with another consumer. To avoid offset commit conflicts, you should usually ensure that the groupId is unique for each consumer instance.
     Note that it isn't possible to mix manual partition assignment (i.e. using assign) with dynamic partition assignment through topic subscription (i.e. using subscribe).

     * @param consumer
     */

    public void manualPartitionAssignment(KafkaConsumer<String, String> consumer) {
        String topic = "foo";
        TopicPartition partition0 = new TopicPartition(topic, 0);
        TopicPartition partition1 = new TopicPartition(topic, 1);
        consumer.assign(Arrays.asList(partition0, partition1));

    }

    /**
     * This example uses commitSync to mark all received messages as committed. In some cases you
     * may wish to have even finer control over which messages have been committed by specifying an
     * offset explicitly. In the example below we commit offset after we finish handling the messages in
     * each partition.
     */

    public void save2DB(KafkaConsumer<String, String> consumer) {
        final int minBatchSize = 200;

        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }

            if (buffer.size() >= minBatchSize) {
                // insertIntoDb(buffer);
                consumer.commitSync();
                buffer.clear();
            }
        }
    }

    public void finedGrainedCommitControl(KafkaConsumer<String, String> consumer) {
        boolean running = true;
        try {
            while(running) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);

                for (TopicPartition partition: records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }

                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();

                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }

    }

    public static void main(String args[]) {

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("foo", "bar"));

    }
}
