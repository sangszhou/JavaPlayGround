package algorithm.sword_2;

/**
 * Created by xinszhou on 21/01/2017.
 */
public class StringToInt {
    public int StrToInt(String str) {
        if (str == null) return 0;
        str = str.trim();
        if (str.equals("")) return 0;

        int sign = 1;
        if (str.charAt(0) == '-') {
            sign = -1;
            str = str.substring(1, str.length());
        } else if (str.charAt(0) == '+') {
            sign = 1;
            str = str.substring(1, str.length());
        }
        str = str.trim();
        if (str.equals("")) return 0;

        int currentNum = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isNum(str.charAt(i))) {
                currentNum = str.charAt(i) - '0' + currentNum * 10;
                if (currentNum < 0) return 0;
            } else {
                return 0;
            }
        }
        return currentNum * sign;
    }

    private boolean isNum(char c) {
        int dis = c - '0';
        if (dis >= 0 && dis <= 9) {
            return true;
        }
        return false;
    }

    public static void main(String args[]) {
        String input = "+123";
        System.out.println(new StringToInt().StrToInt(input));
    }

}
