package algorithm.sword_2;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by xinszhou on 16/12/2016.
 * 这道题又出错了
 */
public class PrintMinNumber {
    public String PrintMinNumber(int[] numbers) {
        String[] newNum = new String[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            newNum[i] = String.valueOf(numbers[i]);
        }

        Arrays.sort(newNum, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String ca1 = o1 + o2;
                String ca2 = o2 + o1;

                return ca1.compareTo(ca2);

            }
        });


        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < newNum.length; i++) {
            sb.append(newNum[i]);
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        int[] array = new int[]{3334, 3};
        String rst = new PrintMinNumber().PrintMinNumber(array);
        System.out.println(rst);
    }

}
