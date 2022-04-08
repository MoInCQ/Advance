public class App {

    public static void main(String[] args) throws Exception {

        // // 反转链表
        // executeReversePrint();

        // 替换空格
        executeReplaceSpace();

    }




     
    // 替换空格
    public static String replaceSpace(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        // 计算出空格数从而推导resultStrChars所需的空间
        int blankCounts = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                blankCounts++;
            }
        }
        int size = s.length() + 2 * blankCounts;
        char[] resultStrChars = new char[size];
        // 替换空格
        for (int i = s.length() - 1, j = resultStrChars.length - 1; i == j; ) {
            if (s.charAt(i) == ' ') {
                resultStrChars[j - 2] = '%';
                resultStrChars[j - 1] = '2';
                resultStrChars[j] = '0';
                j = j - 3; 
                i--;
            } else {
                resultStrChars[j] = s.charAt(i);
                j--;
                i--;
            }
        }
        String resultStr = new String(resultStrChars, 0, size);
        return resultStr;
    }

    public static void executeReplaceSpace() {
        String testStr = "We are happy.";
        replaceSpace(testStr);
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

    public static void executeReversePrint() {
        ListNode temp1 = new ListNode(1);
        ListNode temp2 = new ListNode(3);
        ListNode temp3 = new ListNode(2);
        temp1.next = temp2;
        temp2.next = temp3;
        temp3.next = null;
        reversePrint(temp1);
    }

}

// 链表
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
