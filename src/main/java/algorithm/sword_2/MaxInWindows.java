package algorithm.sword_2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by xinszhou on 17/12/2016.
 */
public class MaxInWindows {
    
    public static void main(String args[]) {
//        int[] array = new int[]{2, 3, 4, 2, 6, 2, 5, 1};
//        new MaxInWindows().maxInWindows(array, 3).forEach(e -> System.out.println(e));

        int[] array2 = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
//        new MaxInWindows().maxInWindows(array2, 4).forEach(e -> System.out.println(e));
    }
    
    public ArrayList<Integer> maxInWindows(int[] num, int size) {
        ArrayList<Integer> rst = new ArrayList<>();
        if (num == null || num.length == 0 || num.length < size || size == 0) return rst;

        Deque<Integer> localMax = new LinkedList<>();

        // stage1: prepare
        for (int i = 0; i < num.length && i < size; i++) {
            while (localMax.size() > 0 && num[i] >= num[localMax.getLast()]) {
                localMax.pollLast();
            }
            localMax.addLast(i);
        }


        rst.add(num[localMax.peek()]);

        for (int i = size; i < num.length; i++) {
            if (i - localMax.peek() >= size) {
                localMax.pop();
            }

            while (localMax.size() > 0 && num[i] >= num[localMax.getLast()]) {
                localMax.pollLast();
            }
            localMax.addLast(i);
            rst.add(num[localMax.peek()]);
        }

        return rst;
    }

}
