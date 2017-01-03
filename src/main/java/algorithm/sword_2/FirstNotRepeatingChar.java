package algorithm.sword_2;

/**
 * Created by xinszhou on 16/12/2016.
 */
public class FirstNotRepeatingChar {

    public int FirstNotRepeatingChar(String str) {
        if (str == null || str.length() <= 0) {
            return -1;
        }

        int[] acc = new int[52];
        for(int i = 0; i < 52; i ++ ) {
            acc[0] = 0;
        }

        for(int i = 0; i < str.length(); i ++) {
            acc[position(str.charAt(i))] += 1;
        }

        for(int i = 0; i < str.length(); i ++) {
            if (acc[position(str.charAt(i))] == 1) {
                return i;
            }
        }

        return 0;
    }

    private int position(char c) {
        if(c >= 'a' && c <= 'z') {
            return c - 'a';
        }

        return c - 'A' + 26;
    }

    public static void main(String args[]) {
        int idx = new FirstNotRepeatingChar().FirstNotRepeatingChar("NXWtnzyoHoBhUJaPauJaAitLWNMlkKwDYbbigdMMaYfkVPhGZcrEwp");
        System.out.println(idx);
    }
}
