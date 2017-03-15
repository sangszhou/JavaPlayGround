package algorithm.cracking;

/**
 * Created by xinszhou on 15/02/2017.
 */
public class Coin {
    public static int countWays(int n) {
        // write code here

        int[] result = new int[n + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
        }
        result[0] = 1;

        for (int i = 1; i <= n; i++) {

            if (i - 25 >= 0) {
                result[i] += result[i - 25];
                result[i] = result[i] % 1000000007;
            }

            if (i - 10 >= 0) {
                result[i] += result[i - 10];
                result[i] = result[i] % 1000000007;
            }

            if (i - 5 >= 0) {
                result[i] += result[i - 5];
                result[i] = result[i] % 1000000007;
            }

            if (i - 1 >= 0) {
                result[i] += result[i - 1];
                result[i] = result[i] % 1000000007;
            }

        }

        return result[n];
    }

    public static void main(String args[]) {
        System.out.println(Coin.countWays(25));

    }

}
