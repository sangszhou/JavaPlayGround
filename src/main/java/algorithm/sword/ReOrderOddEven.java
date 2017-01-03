package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 * 可以使用冒泡方案，但是时间复杂度较高
 */
public class ReOrderOddEven {
    public void reOrderArray(int [] array) {
        if (array == null || array.length <= 0) return;
        int sum = 0;
        for(int i = 0; i < array.length; i ++) {
            if(array[i] % 2 != 0) sum += 1;
        }

        int[] newArray = new int[array.length];

        int cursor1 = 0, cursor2 = sum;
        for(int i = 0; i < array.length; i ++) {
            if(array[i] % 2 == 0) {
                newArray[cursor2++] = array[i];
            } else
                newArray[cursor1++] = array[i];
        }

        for(int i = 0; i < array.length; i ++) {
            array[i] = newArray[i];
        }
    }

    public static void main(String args[]) {
        int[] in = new int[7];
        for(int i = 0; i < in.length; i ++) {
            in[i] = i;
        }

        new ReOrderOddEven().reOrderArray(in);

        for(int i = 0; i < in.length; i++) {
            System.out.println(in[i]);
        }

    }

}
