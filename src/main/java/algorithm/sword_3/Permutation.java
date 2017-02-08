package algorithm.sword_3;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by xinszhou on 05/02/2017.
 */
public class Permutation {

    public ArrayList<String> Permutation(String str) {

        if (str == null || str.length() == 0)
            return new ArrayList<String>();

        char[] chars = new char[str.length()];

        for(int i = 0; i < str.length(); i ++) {
            chars[i] = str.charAt(i);
        }

        ArrayList<String> result = doPermutation(chars, 0, chars.length - 1);
        Collections.sort(result);
        return result;
    }

    public ArrayList<String> doPermutation(char[] input, int start, int end) {
        ArrayList<String> result = new ArrayList<String>();

        // return statement
        if (end == start) {
            result.add(Character.toString(input[start]));
            return result;
        }

        for (int i = start; i <= end; i++) {
            if (input[start] == input[i] && i != start) {
                continue;
            } else {
                swap(input, i, start);
                ArrayList<String> partialResult = doPermutation(input, start + 1, end);

                for (String seq : partialResult) {
                    result.add(Character.toString(input[start]) + seq);
                }

                if(start == 0) {
                    System.out.println("break here");
                }

                //swap back
                swap(input, i, start);
            }
        }

        return result;
    }

    public void swap(char[] input, int i, int j) {
        char temp = input[i];
        input[i] = input[j];
        input[j] = temp;
    }


    public static void main(String args[]) {
        String str = "abc";

        ArrayList<String> result = new Permutation().Permutation(str);

        result.forEach(seq -> System.out.println(seq));
    }
}
