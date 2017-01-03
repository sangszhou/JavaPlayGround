package algorithm.sword_2;

/**
 * Created by xinszhou on 16/12/2016.
 * 数组中的逆序对
 */
public class InversePairs {

    public static void main(String args[]) {
        int[] array = new int[]{1,2,3,4,5,6,7,0};
        int ret = new InversePairs().InversePairs(array);
        System.out.println(ret);
    }


    public int InversePairs(int [] array) {
        int sr = (int) mergeSort(array, 0, array.length - 1);
        return sr;
    }

    public long mergeSort(int[] array, int s, int e) {

        if (s >= e) {
            return 0;
        }

        int mid = (s + e) / 2;

        long l = mergeSort(array, s, mid);
        long r = mergeSort(array, mid + 1, e);

        // combine those two array
        int[] cpa = new int[e - s + 1];

        long rst = 0;
        int i = s;
        int j = mid+1;
        int c = 0;

        while (i <= mid && j <= e) {
            if(array[i] <= array[j]) {
                cpa[c++] = array[i++];
            } else {
                rst += (mid - i+1);
                cpa[c++] = array[j++];
            }
        }

        while (i <= mid) {
            cpa[c++] = array[i++];
        }

        while (j <= e) {
            cpa[c++] = array[j++];
        }


        for(c = 0; c < cpa.length; c ++) {
            array[c+s] = cpa[c];
        }

        return (rst + l + r) % 1000000007;
    }

}
