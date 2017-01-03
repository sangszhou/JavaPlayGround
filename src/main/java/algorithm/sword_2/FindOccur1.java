package algorithm.sword_2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinszhou on 16/12/2016.
 */
public class FindOccur1 {

    public void FindNumsAppearOnce(int [] array,int num1[] , int num2[]) {
        int r1 = 0;
        for(int i = 0; i < array.length; i ++) {
            r1 = r1 ^ array[i];
        }

        int bit = 1;
        while ((bit & r1) == 0) {
            bit *= 2;
        }

        ArrayList<Integer> list1 = new ArrayList<Integer>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();

        for(int i = 0; i< array.length; i ++) {
            if((bit & array[i]) == 0) {
                list1.add(array[i]);
            } else {
                list2.add(array[i]);
            }
        }

        int rst1 = findNumAppearOnce2(list1);
        int rst2 = findNumAppearOnce2(list2);

        num1[0] = rst1;
        num2[0] = rst2;

    }

    private int findNumAppearOnce2(List<Integer> list) {
        int r = 0;
        for (int e : list) {
            r = r ^ e;
        }
        return r;
    }
}
