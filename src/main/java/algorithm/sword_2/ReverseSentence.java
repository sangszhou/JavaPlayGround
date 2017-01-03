package algorithm.sword_2;

/**
 * Created by xinszhou on 19/12/2016.
 */
public class ReverseSentence {
    
    public static void main(String args[]) {
        String str = " ";
        String result = new ReverseSentence().ReverseSentence(str);
        System.out.println( "[" + result + "]");
    }
    
    public String ReverseSentence(String str) {

        if(str == null || str.equals("") || str.equals(" ")) return "";

        String reversed = doReverse(str);
        String[] words = reversed.split(" ");
        for(int i = 0; i < words.length; i ++) {
            words[i] = doReverse(words[i]);
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < words.length; i ++) {
            if (i == words.length - 1) {
                sb.append(words[i]);
            } else {
                sb.append(words[i] + " ");
            }
            
        }
        return sb.toString();
    }

    private String doReverse(String str) {
        char[] chars = str.toCharArray();
        for(int i = 0; i < chars.length / 2; i ++) {
            char temp = chars[i];
            chars[i] = chars[chars.length - i - 1];
            chars[chars.length - i - 1] = temp;
        }
        return new String(chars);
    }

}
