package algorithm.sword_2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by xinszhou on 19/12/2016.
 * a b c -> c' b a' -> b' c a -> b c a 需要多次替换即可
 * 0     ->    s1   ->    s2  ->
 * c++ 可以再 o(1) 的空间内完成，但是 java 的 String 是 immutable 的，所以时间复杂度总是 o(n)
 * 但是可以使用 java 模拟这个过程
 */
public class LeftRotateString {

    // "abcXYZdef" -> "XYZdefabc"
    public String LeftRotateString(String str, int n) {
        //1. what if n > size(str)

        int[] a = new int[3];



        if(n == 0) return str;

        n = n % str.length();
        int rotateSplitPoint = n;

        String leftPart = split(str, 0, n);
        String rightPart = split(str, n, str.length());

        String wholeStr = leftPart + rightPart;
        String result = split(wholeStr, 0, str.length());

        return result;
    }

    // start inclusive, end exclusive
    public String split(String str, int start, int end) {
        char[] chars = new char[end - start];
        end = end - 1;

        System.out.println("start is " + start + ", end is " + end);
        int offset = start;
        while (start <= end) {
            char temp = str.charAt(start);
            chars[start - offset] = str.charAt(end);
            chars[end - offset] = temp;
            start += 1;
            end -= 1;
        }
        return new String(chars);
    }

    public static void main(String args[]) {
        String input1 = "abcXYZdef";
        LeftRotateString so = new LeftRotateString();
        String result = so.LeftRotateString(input1, 5);
        System.out.println(result);
    }
}
