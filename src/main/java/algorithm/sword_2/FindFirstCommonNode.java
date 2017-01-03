package algorithm.sword_2;

import algorithm.sword.ListNode;

/**
 * Created by xinszhou on 16/12/2016.
 */
public class FindFirstCommonNode {
    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        int size1 = 0;
        int size2 = 0;
        ListNode cp1 = pHead1;
        while (cp1 != null) {
            cp1 = cp1.next;
            size1 += 1;
        }

        ListNode cp2 = pHead2;

        while (cp2 != null) {
            cp2 = cp2.next;
            size2 += 1;
        }

        int diff =0;

        if (size2 > size1) {
            diff = size2 -size1;
            cp2 = pHead1;
            cp1 = pHead2;
        } else {
            diff = size1 - size2;
            cp1 = pHead1;
            cp2 = pHead2;
        }

        for(int i = 0; i < diff; i ++) {
            cp1 = cp1.next;
        }

        while (cp1 != null) {
            if(cp1 == cp2) {
                return cp1;
            }
            cp1 = cp1.next;
            cp2 = cp2.next;
        }

        return null;
    }
}
