package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 * what if list is circle ?
 */
public class FindKthToTail {
    public ListNode FindKthToTail(ListNode head, int k) {
        ListNode cursor1 = head;
        ListNode cursor2 = head;

        for (int i = 0; i < k; i++) {
            if (cursor1 != null)
                cursor1 = cursor1.next;
            else
                return null;
        }

        while (cursor1 != null) {
            cursor1 = cursor1.next;
            cursor2 =cursor2.next;
        }
        return cursor2;
    }
}
