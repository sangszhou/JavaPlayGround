package algorithm.sword_2;

/**
 * Created by xinszhou on 16/12/2016.
 *
 * !!! 这道题需要注意当 i == j 时的情况
 */
public class GetNumberOfK {

    public static void main(String args[]) {
        int[] array = new int[]{1, 2, 3, 3, 3, 3};

        int result = new GetNumberOfK().GetNumberOfK(array, 3);
        System.out.println(result);
    }

    public int GetNumberOfK(int[] array, int k) {
        int left = leftMost(array, k);
        int right = rightMost(array, k);

        return right - left + 1;
    }

    public int leftMost(int[] array, int k) {
        int i = 0, j = array.length - 1;

        while (i <= j) {
            int mid = (i + j) / 2;
            if (array[mid] > k) {
                j = mid - 1;
            } else if (array[mid] == k) {
                j = mid - 1;
            } else {
                i = mid + 1;
            }
        }
        return i;

    }

    public int rightMost(int[] array, int k) {
        int i = 0, j = array.length - 1;

        while (i <= j) {
            int mid = (i + j) / 2;
            if (array[mid] > k) {
                j = mid - 1;
            } else if (array[mid] == k) {
                i = mid + 1;
            } else {
                i = mid + 1;
            }
        }
        return j;
    }
}
