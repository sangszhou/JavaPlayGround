package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class NumberOf1 {
    public int NumberOf1(int n) {
        int num = 0;

        while (n != 0) {
            num += 1;
            n = n & (n - 1);
        }
        return num;
    }

}
