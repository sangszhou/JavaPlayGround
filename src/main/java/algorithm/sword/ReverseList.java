package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class ReverseList {

    public ListNode ReverseList(ListNode head) {
        ListNode dummy = new ListNode(-1);

        while (head != null) {
            ListNode temp = head.next;
            head.next = dummy.next;
            dummy.next = head;
            head = temp;
        }

        return dummy.next;
    }

}
