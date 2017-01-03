package algorithm.sword_2;

/**
 * Created by xinszhou on 16/12/2016.
 */
public class FindGreatestSumOfSubArray {
    public int FindGreatestSumOfSubArray(int[] array) {
        int globalMin = array[0];

        int localMin = 0;
        for(int i = 0; i < array.length; i ++) {
            localMin = localMin + array[i];
            if (localMin > globalMin) {
                globalMin = localMin;
            }

            localMin = Math.max(localMin, 0);

        }

        return globalMin;
    }
}
