package algorithm.sword_2;

import java.util.ArrayList;

/**
 * Created by xinszhou on 17/12/2016.
 * 乘积最小是不是蕴涵着第一个发现的结果就是最终结果
 */
public class FindNumbersWithSum {

    public static void main(String args[]) {
        int[] array = new int[]{1,2,4,7,11,15};
        ArrayList<Integer> rst = new FindNumbersWithSum().FindNumbersWithSum(array, 15);
        System.out.println("");
    }

    public ArrayList<Integer> FindNumbersWithSum(int[] array, int sum) {
        ArrayList<Integer> rst = new ArrayList<Integer>();

        if(array == null || array.length == 0) return rst;
        int count = 0;
        int i = 0, j = array.length - 1;
        count += (array[i] + array[j]);

        int c1 = 0, c2 = 0, mul = 999999999;

        while (i <= j) {
            if (count > sum) {
                count -= array[j];
                j--;
                count += array[j];
            } else if (count < sum) {
                count -= array[i];
                i ++;
                count += array[i];
            } else {
                if (array[i] * array[j] < mul) {
                    mul = array[i] *array[j];
                    c1 = array[i];
                    c2 = array[j];
                }
                count -= array[i];
                count -= array[j];
                i ++;
                j --;
                count += array[i];
                count += array[j];
            }
        }
        if(c1 != 0) {

            rst.add(c1);
            rst.add(c2);
        }
        return rst;
    }

}
