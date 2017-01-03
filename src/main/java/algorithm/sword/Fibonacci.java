package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 *
 * 不使用 record 的记录本算法
 */
public class Fibonacci {

    public int Fibonacci(int n) {
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1;

        int last = 1;
        int lastlast = 1;

        int result = 2;
        for(int i = 3; i <= n; i ++) {
            result = lastlast + last;
            lastlast = last;
            last = result;
        }

        return result;
    }

    public static void main(String args[]) {
        // 0, 1, 1, 2, 3, 5, 8
        Fibonacci fib = new Fibonacci();
        int result = fib.Fibonacci(6);
        System.out.println(result);
    }
}
