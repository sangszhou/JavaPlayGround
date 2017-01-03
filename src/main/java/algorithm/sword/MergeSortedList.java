package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class MergeSortedList {
    public ListNode Merge(ListNode list1,ListNode list2) {
        ListNode newHead = new ListNode(-1);
        ListNode cursor = newHead;

        while(list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                cursor.next = list1;
                list1 = list1.next;
                cursor = cursor.next;
            } else {
                cursor.next = list2;
                list2 = list2.next;
                cursor = cursor.next;
            }
        }

        if (list1 != null) {
            cursor.next = list1;
        }

        if (list2 != null) {
            cursor.next = list2;
        }

        return newHead.next;
    }
}
