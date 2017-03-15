package algorithm.cracking;

import algorithm.sword.ListNode;

/**
 * Created by xinszhou on 24/02/2017.
 */
public class Partition {
    public ListNode partition(ListNode pHead, int x) {
        ListNode p1 = new ListNode(0);
        ListNode p2 = new ListNode(0);

        ListNode cp1 = p1;
        ListNode cp2 = p2;

        while (pHead != null) {
            if (pHead.val < x) {
                cp1.next = pHead;
                cp1 = cp1.next;
            } else {
                cp2.next = pHead;
                cp2 = cp2.next;
            }
            pHead = pHead.next;
        }
        cp1.next = null;
        cp2.next = null;

        cp1 = p1;

        while (cp1.next != null) {
            cp1 = cp1.next;
        }

        cp1.next = p2.next;

        return p1.next;
    }

}
