public class App {

    public static void main(String[] args) throws Exception {
        ListNode temp1 = new ListNode(1);
        ListNode temp2 = new ListNode(3);
        ListNode temp3 = new ListNode(2);
        temp1.next = temp2;
        temp2.next = temp3;
        temp3.next = null;
        reversePrint(temp1);
    }



    // 反转链表后输出
    public static int[] reversePrint(ListNode head) {
        // 先原地反转链表
        ListNode pre = null;
        ListNode curr = head;
        int size = 0;
        while (curr != null) {           
            ListNode next = curr.next;
            curr.next = pre;
            pre = curr;
            curr = next;
            size++;
        }
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = pre.val;
            pre = pre.next;
        }
        return result;
    }

}

// 链表
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
