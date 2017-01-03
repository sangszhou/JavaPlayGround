package algorithm.sword_2;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by xinszhou on 17/12/2016.
 */
public class MedianInStream {

    public static void main(String args[]) {
        MedianInStream s = new MedianInStream();

        int[] arr = new int[]{5,2,3,4,1,6,7,0,8};
        for(int i = 0; i < arr.length; i ++) {
            s.Insert(arr[i]);
            System.out.println(s.GetMedian());
        }

        System.out.println(s.GetMedian());
    }


    // 小根堆，存储大数据
    public PriorityQueue<Integer> minQ = new PriorityQueue<Integer>(1, new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    });

    // 大根堆，存储小数据
    public PriorityQueue<Integer> maxQ = new PriorityQueue<Integer>(1, new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    });

    public void Insert(Integer num) {
        if(minQ.size() == 0) {
            minQ.add(num);
            return;
        } else if(num > minQ.peek()){
            minQ.add(num);
        } else {
            maxQ.add(num);
        }
        // balance

        if (minQ.size() - maxQ.size() >= 2) {
            maxQ.add(minQ.poll());
        } else if (maxQ.size() - minQ.size() >= 2) {
            minQ.add(maxQ.poll());
        }
    }

    public Double GetMedian() {
        if ((minQ.size() + maxQ.size()) % 2 == 0) {
            return (minQ.peek() + maxQ.peek()) * 1.0 / 2;
        } else if(minQ.size() > maxQ.size()){
            return minQ.peek() * 1.0;
        } else {
            return maxQ.peek() * 1.0;
        }
    }

}
