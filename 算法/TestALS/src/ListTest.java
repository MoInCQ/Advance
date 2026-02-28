import java.util.*;

public class ListTest {
    public static void main(String args[]) {
        ListTest instance = new ListTest();

//        ListNode head = new ListNode(1);
//        ListNode rear = new ListNode(2);
//        head.next = rear;
//        instance.removeNthFromEnd(head, 2);



    }

    // 相交链表 https://leetcode.cn/problems/intersection-of-two-linked-lists/?envType=study-plan-v2&envId=top-100-liked
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // 法1:哈希表，遍历存A，遍历B查哈希表里有没有相同元素（有相同元素，则后面的都相同，因为是同一个实例）
//        Set<ListNode> visited = new HashSet<ListNode>();
//        ListNode temp = headA;
//        while (temp != null) {
//            visited.add(temp);
//            temp = temp.next;
//        }
//        temp = headB;
//        while (temp != null) {
//            if (visited.contains(temp)) {
//                return temp;
//            }
//            temp = temp.next;
//        }
//        return null;


        // 法2:双指针
        // 设l是可能相交的部分，a为A单独的部分，b为B单独的部分
        // 那么链表A的长度为a + l，链表B的长度为b + l
        // 那么指针A遍历，结束后遍历B，另一个也是，长度都应该是a+b+2l
        // 如果存在相交则 a + l + b  = b + l + a
        // 所以结束态要么同时到null 要么找到节点 故 while循环条件为temp1 = temp2
        ListNode temp1 = headA;
        ListNode temp2 = headB;
        while (temp1 != temp2) {
            temp1 = temp1 != null ? temp1.next : headB; // 一定注意这里判的是自己是不是null，如果是判temp.next的话 temp就永远空不了 ，转一次之后就死循环了
            temp2 = temp2 != null ? temp2.next : headA;
        }
        return temp1; // 随便return一个temp就行，因为如果不相交这里两个都是null, 相交则两个都一样
    }

    // 反转链表 https://leetcode.cn/problems/reverse-linked-list/?envType=study-plan-v2&envId=top-100-liked
    public ListNode reverseList(ListNode head) {
        if (head == null) return null;
        // 双指针 （一定记得是 【pre】 和 【🌟cur（如果是next会导致最后一个设置不到next）】，）
        ListNode pre = null;
        ListNode cur = head; // 当前处理的节点
        while (cur != null) {   // 🌟四步走
            // 记录原本的下一个节点
            ListNode next = cur.next;
            // 断链指向新的next
            cur.next = pre;
            // 移动pre
            pre = cur;
            // 移动cur
            cur = next;
        }
        return pre;  // 最后cur = null， 输出原最后一个节点
    }


    // 回文链表 https://leetcode.cn/problems/palindrome-linked-list/?envType=study-plan-v2&envId=top-100-liked
    public boolean isPalindrome(ListNode head) {
        // 把链表的值都读到数组里，然后头尾指针往中间移依次比较
        List<Integer> tempList = new ArrayList<>();
        while (head != null) {
            tempList.add(head.val);
            head = head.next;
        }
        int start = 0; int end = tempList.size() - 1;
        while (start < end) {
            if (tempList.get(start) != tempList.get(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }


    // 环形链表 https://leetcode.cn/problems/linked-list-cycle/?envType=study-plan-v2&envId=top-100-liked
    public boolean hasCycle(ListNode head) {
//        // 法1: 哈希set判断
//        Set<ListNode> visitedSet = new HashSet<>();
//        while (head != null) {
//            if (visitedSet.contains(head)) {
//                return true;
//            }
//            visitedSet.add(head);
//            head = head.next;
//        }
//        return false;


        // 法2：快慢指针
        // 有环则总会相遇  （相遇的地方不一定是环的起点, 参考TrickTest）
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null) {  // 这里只需要判定fast就行了，如果没有环，fast最后一定是null
            slow = slow.next;
            if (fast.next == null) {
                break;
            }
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }


    // 环形链表2 找环的起点 https://leetcode.cn/problems/linked-list-cycle-ii/?envType=study-plan-v2&envId=top-100-liked
    public ListNode detectCycle(ListNode head) {
        // 法1: hash表第一个重复的位置就是环的起点
        // 法2: 快慢指针（弗洛伊德算法）
        // a 是 起点到环的起点的位置
        // b 是 环的起点到快慢指针相交的位置
        // l 是环的长度
        // 快指针走了 a + b + nl 慢指针走了 a + b
        // ------- 阶段1end:找到相遇点
        // ------- 阶段2开始：找环起点
        // 因为快指针的速度是慢指针的两倍，所以a + b + nl = 2  * (a + b)
        // 所以 a+b = nl  --->   a = nl - b
        // 此时将慢指针重新从0开始走 走a步到环起点
        // 【快指针走 nl - b步到环起点（此时快指针在相遇点 b， nl - b 刚好快指针每次走一步的话 回到起点 的过程），慢指针也是走a步到环起点】
        // 所以此时快慢指针一起走，【相遇】的时候就是环起点
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null) {
            slow = slow.next;
            if (fast.next == null) {
                return null;
            }
            fast = fast.next.next;
            if (slow == fast) {
                // 找到相遇点，说明有环，开始找环的起点
                slow = head;
                while (slow != fast) {
                    // 让slow和fast走相同的步数即可
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow; // 找到环的起点（返回谁都行，因为slow = fast）
            }
        }
        return null; // 没有环
    }

    // 合并两个有序链表 https://leetcode.cn/problems/merge-two-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // 构建起点
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode preHead = new ListNode(-1); // 头指针的前置，方便构建初始状态，并记录返回值 需要设置一个虚拟节点（不能是null）
        ListNode cur = preHead;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                cur.next = list1;
                list1 = list1.next;
            } else {
                cur.next = list2;
                list2 = list2.next;
            }
            // 移动当前指针
            cur = cur.next;
        }
        if (list1 != null) {
            cur.next = list1;
        }
        if (list2 != null) {
            cur.next = list2;
        }
        return preHead.next;
    }

    // 合并 K 个升序链表 https://leetcode.cn/problems/merge-k-sorted-lists/?envType=study-plan-v2&envId=top-100-liked
    public ListNode mergeKLists(ListNode[] lists) {
        // 法1:两两合并
        ListNode result = null;
        for (ListNode node : lists) {
            result = mergeTwoLists(result, node);
        }
        return result;

//        // 法2:优先级队列，
//        // （1）先把list中的head 比较顺序，然后依次入队
//        // （2）出队的时候比较队头元素和出队节点的next，直至队列为空
//
//        if (lists == null || lists.length == 0) {
//            return null;
//        }
//
//        // 创建优先级队列（最小堆），按节点值排序
//        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
//
//        // 步骤1：把所有链表的头节点入队
//        for (ListNode head : lists) {
//            if (head != null) {  // 注意：跳过空链表
//                pq.offer(head);
//            }
//        }
//
//        // 创建虚拟头节点
//        ListNode dummy = new ListNode(0);
//        ListNode cur = dummy;
//
//        // 步骤2：出队最小节点，连接结果链表，并将next入队
//        while (!pq.isEmpty()) {
//            // 出队最小的节点
//            ListNode minNode = pq.poll();
//
//            // 连接到结果链表
//            cur.next = minNode;
//            cur = cur.next;
//
//            // 如果出队节点的next不为null，将next入队
//            if (minNode.next != null) {
//                pq.offer(minNode.next);
//            }
//        }
//
//        return dummy.next;
    }

    // 删除链表的倒数第 N 个结点 https://leetcode.cn/problems/remove-nth-node-from-end-of-list/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // 法1: 反转链表 删除第n个 再反转
        // 法2: 获取到链表长度，再从头删除第 length - n 个位置
//        if (head == null || n <= 0) {
//            return null;
//        }
//        int length = 0;
//        ListNode cur = head;
//        while (cur != null) {
//            length++;
//            cur = cur.next;
//        }
//        // 此时得到了length长度
//        cur = head;
//        ListNode pre = new ListNode(-1);
//        pre.next = cur;
//        for (int i = 0; i < length - n; i++) {  // 跳 length - n次
//            pre = pre.next;
//            cur = cur.next;
//        }
//        // 找到要断的节点
//        if (cur == head) return head.next; // 处理特殊边界值
//        pre.next = cur.next;
//        cur.next = null;
//        cur = null;
//        return head;

        // 法3: 快慢指针
        ListNode pre = new ListNode();
        pre.next = head;
        ListNode fast = head;    // key: 快指针的起点就是当前节点
        ListNode slow = pre;     // key：慢指针的起点是 pre🌟 因为要找到 n前面那个节点去摘链
        // 快指针先走n步
        for (int i = 0; i < n; ++i) {
            fast = fast.next;
        }
        // 慢指针和快指针一起走，直到快指针走到null
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;  // 🌟 删除节点的过程，其实就是换个指针指向
        return pre.next;

    }

    // 两数相加 https://leetcode.cn/problems/add-two-numbers/?envType=study-plan-v2&envId=top-100-liked
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 相加， 【🌟产生进位】则记录
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        ListNode resultPreHead = new ListNode();
        ListNode resultCur = resultPreHead;
        boolean needAdd1 = false; // 是否进位
        while (l1 != null || l2 != null) {  // 🌟要考虑多级进位，尽量在一个循环里写完，避免重复写进位逻辑
            resultCur.next = new ListNode();
            resultCur = resultCur.next;

            int curSum = (l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0);

            if (needAdd1) {
                curSum++;
                needAdd1 = false;
            }
            if (curSum >= 10) {
                needAdd1 = true;
            }
            // 计算值
            resultCur.val = curSum % 10;
            // 设置next
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (needAdd1) {
            resultCur.next = new ListNode(1);  // 最后一个进位
        }
        return resultPreHead.next;
    }

    // 两两交换链表中的节点 https://leetcode.cn/problems/swap-nodes-in-pairs/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode swapPairs(ListNode head) {
        // 【🌟三指针】，pre cur next  一次替换后，全部=pointer.next.next
        // 如果 nextPointer.next == null || nextPointer.next.next == null 则无需交换了
        // 处理下特殊值：head = null 和 head.next = null;
        if (head == null || head.next == null) {
            return head;
        }
        ListNode preHead = new ListNode();
        preHead.next = head;
        ListNode prePointer = preHead;
        ListNode curPointer = head;
        ListNode nextPointer = head.next;
        while (nextPointer != null) {
            // 交换
            prePointer.next = nextPointer;
            curPointer.next = nextPointer.next;
            nextPointer.next = curPointer;
            // 预置下一次
            prePointer = curPointer;
            curPointer = curPointer.next;
            if (curPointer == null) {
                break;
            }
            nextPointer = curPointer.next;
        }
        return preHead.next;
    }

    // 排序链表 https://leetcode.cn/problems/sort-list/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode sortList(ListNode head) {
        // 【🌟转成数组，排序，再转成链表】
        if (head == null || head.next == null) {
            return head;
        }
        // 1、构建数组
        List<Integer> arr = new ArrayList<>();
        while (head != null) {
            arr.add(head.val);
            head = head.next;
        }
        // 2、数组排序
        Collections.sort(arr);
        // 3、链表重建
        ListNode preHead = new ListNode();
        ListNode cur = preHead;              // 🌟 cur指向preHead就行，让循环初始值可以正常运转
        for (int i = 0; i < arr.size(); i++) {
            ListNode next = new ListNode(arr.get(i));
            // 接上链
            cur.next = next;
            // 预置下一次
            cur = cur.next;
        }
        return preHead.next;
    }


    // LRUCache https://leetcode.cn/problems/lru-cache/description/?envType=study-plan-v2&envId=top-100-liked
    class LRUCache {
        // LRU（最近最少使用）：（1）一个独立的🌟哈希表（确保O（1）时间访问，key：key，value：链表节点）  （2） 一个独立的🌟双向链表（O(1) 插入、删除、移动节点——链表头入，链表尾出）
        // LFU（最少使用）：（1） 哈希表findMap +  （2）🌟frquenceMap（key：频率，value：该频率下的双向链表）+ （3）🌟minFreq 最小使用变量（用于结合freqMap O（1）时间找出最少使用的node）
        class DLListNode {  // 双向链表节点
            int key;     // 摘链时 O（1）删除findMap内容
            int value;
            DLListNode pre;
            DLListNode next;
            DLListNode() {};
            DLListNode(int key, int value) { this.key = key; this.value = value; }
        }

        Map<Integer, DLListNode> findMap;
        DLListNode preHead;  // 头前指针(🌟用于头插——最近用的)
        DLListNode postTail; // 尾后指针（🌟用于尾出——最近没用的）
        int capacity; // 缓存容量

        public LRUCache(int capacity) {
            findMap = new HashMap<>();
            preHead = new DLListNode();
            postTail = new DLListNode();
            preHead.next = postTail;
            postTail.pre = preHead;
            this.capacity = capacity;
        }

        public int get(int key) {
            // (1) 取值
            DLListNode targetNode = findMap.get(key);
            if (targetNode == null) {
                return -1;
            }
            // （2）移动到链表头部，更新其为最近最常使用
            moveToHead(targetNode);
            int result = targetNode.value;
            return result;
        }

        public void put(int key, int value) {

            // 两个存储结构都要更新
            DLListNode targetNode = findMap.get(key);
            if (targetNode == null) {
                // （1）无值放值
                targetNode = new DLListNode(key, value);
                findMap.put(key, targetNode);

                addToHead(targetNode);   // （3）添加到头节点

                // （4）如果超出阈值，删除尾部节点
                if (findMap.size() > capacity) {
                    DLListNode recentlyNotUsedNode = popTailNode();
                    if (recentlyNotUsedNode == null) {
                        return;
                    }
                    findMap.remove(recentlyNotUsedNode.key);
                }
            } else {
                // （2）有值更新
                targetNode.value = value;

                moveToHead(targetNode);  // （3）更新到头节点
            }
        }

        private void moveToHead(DLListNode node) {
            if (node == null) {
                return;
            }
            // 先摘链
            node.pre.next = node.next;
            node.next.pre = node.pre;

            // 再头插
            addToHead(node);
        }

        private void addToHead(DLListNode node) {
            // （1）插入
            node.next = preHead.next;
            node.pre = preHead;
            // （2）更新原有节点的链接关系
            preHead.next = node;
            node.next.pre = node;
        }

        private DLListNode popTailNode() {
            if (postTail.pre == preHead) {
                return null;
            }
            DLListNode deleteNode = postTail.pre;
            // （1）更改链接关系
            deleteNode.pre.next = postTail;
            postTail.pre = deleteNode.pre;
            // （2）摘链 (不摘也行)
            deleteNode.next = null;
            deleteNode.pre = null;
            return deleteNode;
        }
    }


//    LRU vs LFU
//LRU（最近最少使用）：
//单个双向链表 + 哈希表
//淘汰策略：删除最久未访问的元素
//时间复杂度：O(1)
//LFU（最不经常使用）：
//多个双向链表（按频率分组）+ 两个哈希表（keyMap + freqMap）+ minFreq变量
//淘汰策略：删除访问频率最低的元素，频率相同时删除最久未使用的
//时间复杂度：O(1)
//关键点：
//freqMap：每个频率对应一个双向链表，同一频率的节点按LRU顺序组织
//minFreq：跟踪当前最小频率，用于快速找到要淘汰的节点
//updateFreq：每次访问节点时，将其从旧频率链表移到新频率链表头部
//淘汰策略：从minFreq对应的链表尾部删除节点（频率最低且最久未使用）


    // LFU实现
    class LFUCache {

        // LFU（最少使用）：
        // 1. keyMap: 哈希表（key：key，value：链表节点，节点包含key、value、freq）
        // 2. freqMap: 哈希表（key：频率，value：该频率对应的双向链表）
        // 3. minFreq: 最小频率变量（用于O(1)时间找出最少使用的node）

        class DLListNode {  // 双向链表节点
            int key;
            int value;
            int freq;    // 🌟新增：访问频率
            DLListNode pre;
            DLListNode next;

            DLListNode() {}

            DLListNode(int key, int value) {
                this.key = key;
                this.value = value;
                this.freq = 1;  // 初始频率为1
            }
        }

        class DLList {  // 双向链表（用于管理同一频率的节点）
            DLListNode preHead;   // 头前指针
            DLListNode postTail;  // 尾后指针

            DLList() {
                preHead = new DLListNode();
                postTail = new DLListNode();
                preHead.next = postTail;
                postTail.pre = preHead;
            }

            // 添加到链表头部（最近使用）
            void addToHead(DLListNode node) {
                node.next = preHead.next;
                node.pre = preHead;
                preHead.next.pre = node;
                preHead.next = node;
            }

            // 移除节点
            void removeNode(DLListNode node) {
                node.pre.next = node.next;
                node.next.pre = node.pre;
            }

            // 弹出尾部节点（最久未使用）
            DLListNode popTail() {
                if (postTail.pre == preHead) {
                    return null;
                }
                DLListNode deleteNode = postTail.pre;
                removeNode(deleteNode);
                return deleteNode;
            }

            // 判断链表是否为空
            boolean isEmpty() {
                return preHead.next == postTail;
            }
        }

        Map<Integer, DLListNode> keyMap;      // key -> 节点
        Map<Integer, DLList> freqMap;         // 🌟频率 -> 该频率的双向链表
        int capacity;                          // 缓存容量
        int minFreq;                          // 🌟当前最小频率

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.minFreq = 0;
            keyMap = new HashMap<>();
            freqMap = new HashMap<>();
        }

        public int get(int key) {
            // （1）取值
            DLListNode node = keyMap.get(key);
            if (node == null) {
                return -1;
            }

            // （2）🌟更新频率（从旧频率链表移到新频率链表头部）
            updateFreq(node);

            return node.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) {
                return;
            }

            DLListNode node = keyMap.get(key);

            if (node != null) {
                // （1）有值更新
                node.value = value;
                // （2）🌟更新频率
                updateFreq(node);
            } else {
                // （3）无值放值

                // （4）如果超出容量，删除最小频率的尾部节点
                if (keyMap.size() >= capacity) {
                    // 获取最小频率对应的链表
                    DLList minFreqList = freqMap.get(minFreq);
                    // 删除该链表的尾部节点（最久未使用）
                    DLListNode deleteNode = minFreqList.popTail();
                    if (deleteNode != null) {
                        keyMap.remove(deleteNode.key);
                    }
                }

                // （5）创建新节点，频率为1
                DLListNode newNode = new DLListNode(key, value);
                keyMap.put(key, newNode);

                // （6）添加到频率为1的链表头部
                DLList list = freqMap.getOrDefault(1, new DLList());
                list.addToHead(newNode);
                freqMap.put(1, list);

                // （7）🌟重置最小频率为1
                minFreq = 1;
            }
        }

        // 🌟更新节点频率（核心方法）
        private void updateFreq(DLListNode node) {
            int oldFreq = node.freq;

            // （1）从旧频率链表中移除
            DLList oldList = freqMap.get(oldFreq);
            oldList.removeNode(node);

            // （2）如果旧频率链表为空且等于minFreq，minFreq需要+1
            if (oldList.isEmpty() && oldFreq == minFreq) {
                minFreq++;
            }

            // （3）节点频率+1
            node.freq++;

            // （4）添加到新频率链表的头部
            DLList newList = freqMap.getOrDefault(node.freq, new DLList());
            newList.addToHead(node);
            freqMap.put(node.freq, newList);
        }
    }


    // 随机链表的复制 https://leetcode.cn/problems/copy-list-with-random-pointer/description/?envType=study-plan-v2&envId=top-100-liked
    class Node {  // 随机链表节点
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }
    // 【🌟递归分解】所需要的缓存（key：老节点，value：新节点）
    // 因为random节点导致创建随机所以才需要这个map结构进行o1查找，如果是单链表则不用，直接迭代就行了
    Map<Node, Node> cachedRandomNodeMap = new HashMap<>();
    public Node copyRandomList(Node head) {   // 实际上是一个查找，【🌟通过旧节点查找新节点】的的递归函数
        // 法1: 哈希表（确保O（1）时间找到对应的节点，并缓存结果用于后续设置next和random指针）
        if (head == null) return null;
        Node targetNode = cachedRandomNodeMap.get(head);
        if (targetNode == null) {
            targetNode = new Node(head.val);
            cachedRandomNodeMap.put(head, targetNode);
            targetNode.next = copyRandomList(head.next);                   // 🌟先完成了dfs遍历一遍  填充完了所有节点map（完成了创建 + 遍历的过程 ）
            targetNode.random = cachedRandomNodeMap.get(head.random);      // 在上述基础上 【直接查找】 一定能找到对应的值
        }
        return targetNode;

        // 法2: hash表存储新旧节点  + 两次循环 （第一次 依次copy node 存储到hash表里，第二次循环，依次设置next和random指针）


        // 延伸：普通的单链表的复制
//        public ListNode copySimpleList(ListNode head) {
//            if (head == null) {
//                return null;
//            }
//
//            // 创建虚拟头节点
//            ListNode dummy = new ListNode(0);
//            ListNode cur = dummy;            // 🌟 cur指向preHead就行，让循环初始值可以正常运转
//            ListNode original = head;
//
//            // 遍历原链表，逐个复制节点
//            while (original != null) {
//                cur.next = new ListNode(original.val);
//                cur = cur.next;
//                original = original.next;
//            }
//
//            return dummy.next;
//        }
    }
}



