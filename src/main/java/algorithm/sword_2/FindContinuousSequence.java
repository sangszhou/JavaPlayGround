package algorithm.sword_2;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by xinszhou on 17/12/2016.
 */
public class FindContinuousSequence {

    public static void main(String args[]) {
        ArrayList<ArrayList<Integer>> rst = new FindContinuousSequence().FindContinuousSequence(100);
        System.out.println("");
    }

    public ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> rst = new ArrayList<ArrayList<Integer>>();

        int i = 1, j = 2;
        int count = 3;

        while (i <= j && j <= sum) {
            if (count > sum) {
                count -= i;
                i ++;
            } else if (count == sum) {
                ArrayList<Integer> local = new ArrayList<Integer>();

                for(int n = i; n <= j; n ++) {
                    local.add(n);
                }
                rst.add(local);
                count = count - i;
                i ++;
                j ++;
                count = count + j;
            } else { // count < sum
                j ++;
                count += j;
            }
        }
        return rst;
    }

}
