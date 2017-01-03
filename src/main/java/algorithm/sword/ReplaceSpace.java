package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class ReplaceSpace {
    // %20
    public String replaceSpace(StringBuffer str) {
        int numOfSpace = 0;
        for(int i = 0; i < str.length(); i ++) {
            if(str.charAt(i) == ' ')
                numOfSpace += 1;
        }

        char[] replaced = new char[str.length() + numOfSpace * 2];

        for(int i = 0, j = 0; i < replaced.length; i ++, j++) {
            if(str.charAt(j) == ' ') {
                replaced[i] = '%';
                replaced[i + 1] = '2';
                replaced[i + 2] = '0';
                i += 2;
            } else {
                replaced[i] = str.charAt(j);
            }
        }

        return new String(replaced);
    }
    
    public static void main(String args[]) {
        String origin = "We Are Happy";
        String result = (new ReplaceSpace()).replaceSpace(new StringBuffer(origin));
        System.out.println(result);
    }

}
