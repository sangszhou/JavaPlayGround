package algorithm.sword;

import java.util.ArrayList;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class PrintListFromTailToHead {


    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        if (listNode == null) {
            return new ArrayList<Integer>();
        } else {
            ArrayList<Integer> result = printListFromTailToHead(listNode.next);
            result.add(listNode.val);
            return result;
        }
    }

}
