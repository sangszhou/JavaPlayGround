package algorithm.sword_2;

/**
 * Created by xinszhou on 19/12/2016.
 * a b c -> c' b a' -> b' c a -> b c a 需要多次替换即可
 * 0     ->    s1   ->    s2  ->
 * c++ 可以再 o(1) 的空间内完成，但是 java 的 String 是 immutable 的，所以时间复杂度总是 o(n)
 * 但是可以使用 java 模拟这个过程
 */
public class LeftRotateString {
    public static void main(String args[]) {
        String rst = new LeftRotateString().LeftRotateString("abcdefg", 6);
        System.out.println(rst);
    }
    public String LeftRotateString(String str,int n) {

//        if (n * 2 > str.length()) {
//            n = str.length() - n;
//        }

        String part1 = str.substring(0, n);
        String part2 = str.substring(n, str.length() - n);
        String part3 = str.substring(str.length() - n, str.length());

        return part2 + part3 + part1;
    }

}
