package algorithm.ali.quotaQueue;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by xinszhou on 23/02/2017.
 */
public class ManagedQueue {

    // 问题，假如 99% 的 quota 都给了一个人，那么循环的次数会比较多
    // 另外一个办法是向一个 list 中添加 quota, 这样能够防止空转，但是消耗的内存资源会比较多
    // 还有一种办法是 dynamic 的数据结构，只有

    // quota -> navigationMap
    NavigableMap<Integer, String> quotaMap = new ConcurrentSkipListMap<>();
    Map<String, Queue<Task>> taskQueues = new HashMap<>();

    public synchronized void enqueue(String userId, Task task) {
        Queue<Task> queue = taskQueues.get(userId);
        if (queue == null) {
            taskQueues.put(userId, new ConcurrentLinkedDeque<>());
            queue = taskQueues.get(userId);
        }
        queue.add(task);
    }

    public synchronized Task dequeue() {
        Task result = null;

        while (result == null) {
            int rand = (int) Math.random() * 100;
            int ceilingKey = quotaMap.ceilingKey(rand);
            String userId = quotaMap.get(ceilingKey);

            if (taskQueues.get(userId).size() <= 0) {
                continue;
            } else {
                result = taskQueues.get(userId).poll();
            }

        }
        return result;
    }
}
