import java.util.*;


public class BinaryTreeTest {

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {

    }


    // 二叉树的中序遍历 https://leetcode.cn/problems/binary-tree-inorder-traversal/description/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> inorderTraversal(TreeNode root) {
        // 法1:递归，时间O（n），空间为O（层数）在退化为单链表时最大为O（n）
        List<Integer> result = new ArrayList<>();
        inOrder(root, result); // 🌟新建一个方法传递resultMap进行递归，避免每次都重建
        return result;

        // 法2:迭代，通过栈显示模拟递归栈
//        List<Integer> res = new ArrayList<Integer>();
//        Deque<TreeNode> stk = new LinkedList<TreeNode>();
//        while (root != null || !stk.isEmpty()) {
//            while (root != null) {
//                stk.push(root);          // 先把左边全压栈
//                root = root.left;
//            }
//            root = stk.pop();           // 出栈读取（第一个是最左叶子节点）
//            res.add(root.val);          // 添加节点（root变换概念，仅添加root）
//            root = root.right;          // root指向右边 （左边和中间遍历完了）
//        }
//        return res;

    }

    private void inOrder(TreeNode root, List<Integer> resultList) {
        if (root == null) {
            return;
        }
        inOrder(root.left, resultList);
        resultList.add(root.val);
        inOrder(root.right, resultList);
    }

    // 非递归前序遍历
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);  // 先访问根节点

            // 先压右子树，再压左子树（这样左子树会先被弹出）
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }

        return result;
    }


    // 非递归后序遍历
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        TreeNode prev = null; // 记录上一个访问的节点

        while (curr != null || !stack.isEmpty()) {
            // 一直向左走到底
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            // 查看栈顶元素
            curr = stack.peek();

            // 如果右子树为空或已经访问过右子树，则访问当前节点
            if (curr.right == null || curr.right == prev) {
                result.add(curr.val);
                stack.pop();
                prev = curr;  // 记录已访问的节点
                curr = null;  // 重置curr，继续处理栈中的节点
            } else {
                // 否则访问右子树
                curr = curr.right;
            }
        }

        return result;
    }




    // 二叉搜索树中第 K 小的元素 https://leetcode.cn/problems/kth-smallest-element-in-a-bst/description/?envType=study-plan-v2&envId=top-100-liked
    public int kthSmallest(TreeNode root, int k) {
        // 【key：🌟二叉搜索树 中序遍历 是 升序的 】成ArrayList ---> 升序数组，然后取下标
        if (root == null || k <= 0) { return -1; }
        List<Integer> inOrderList = inorderTraversal(root);
        return inOrderList.get(k - 1); // 因为是从1开始计数，所以读数组要-1
    }



    // 二叉树的最大深度 https://leetcode.cn/problems/maximum-depth-of-binary-tree/description/?envType=study-plan-v2&envId=top-100-liked
    public int maxDepth(TreeNode root) {
        // 🌟 【递归】找Max(左子树高度，右子树高度) + 1;
        // 时间 O(n) 空间 O(height)
        if (root == null) { return 0; }
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        return Math.max(leftDepth, rightDepth) + 1;
    }

    // 非递归求高度
    public class BinaryTreeHeight {

        // 辅助类，记录节点及其深度
        static class NodeWithDepth {
            TreeNode node;
            int depth;

            NodeWithDepth(TreeNode node, int depth) {
                this.node = node;
                this.depth = depth;
            }
        }

        /**
         * DFS非递归求树高度 - 使用栈 + 深度标记
         */
        public int maxDepthDFS(TreeNode root) {
            if (root == null) {
                return 0;
            }

            Stack<NodeWithDepth> stack = new Stack<>();
            stack.push(new NodeWithDepth(root, 1));
            int maxDepth = 0;

            while (!stack.isEmpty()) {
                NodeWithDepth current = stack.pop();
                TreeNode node = current.node;
                int depth = current.depth;

                // 更新最大深度
                maxDepth = Math.max(maxDepth, depth);

                // 先压右子树，再压左子树（DFS遍历）
                if (node.right != null) {
                    stack.push(new NodeWithDepth(node.right, depth + 1));
                }
                if (node.left != null) {
                    stack.push(new NodeWithDepth(node.left, depth + 1));
                }
            }

            return maxDepth;
        }
    }





    // 二叉树的直径 https://leetcode.cn/problems/diameter-of-binary-tree/description/?envType=study-plan-v2&envId=top-100-liked
    private int maxDiameter = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        // 目标直径 =  左子树最大深度 + 右子树最大深度
        if (root == null) return 0;
        maxDiameter = 0; // 每次调用重置下
        getMaxDepth(root); // 在找最大深度的时候同时记录最大直径
        return maxDiameter;
    }
    private int getMaxDepth(TreeNode root) {
        if (root == null) return 0;
        int rightMaxDepth = getMaxDepth(root.right);
        int leftMaxDepth = getMaxDepth(root.left);
        maxDiameter = Math.max(rightMaxDepth + leftMaxDepth, maxDiameter); // 计算经过当前根节点的最大直径的路径的节点数（刚好就是最大直径）
        return Math.max(rightMaxDepth, leftMaxDepth)  + 1; // 加1是加根节点，即获取最大高度
    }




    // 翻转二叉树 https://leetcode.cn/problems/invert-binary-tree/description/?envType=study-plan-v2&envId=top-100-liked
    public TreeNode invertTree(TreeNode root) {
        // 【🌟递归 --> 转换为单个节点问题 】反转
        if (root == null) return null;
        TreeNode temp = root.left;               // 🌟因为后面要修改root.left了 所以需要保存一下原本的左子树
        root.left = invertTree(root.right);
        root.right = invertTree(temp);
        return root;
    }

    /**
     * 非递归反转二叉树 - 使用 【栈（DFS）】
     */
    public TreeNode invertTreeDFS(TreeNode root) {
        if (root == null) {
            return null;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();

            // 交换左右子节点
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;

            // 将左右子节点入栈继续处理
            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }

        return root;
    }

    public class InvertBinaryTree {

        /**
         * 非递归反转二叉树 - 使用 【队列（BFS）】
         */
        public TreeNode invertTreeBFS(TreeNode root) {
            if (root == null) {
                return null;
            }

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();

                // 交换左右子节点
                TreeNode temp = node.left;
                node.left = node.right;
                node.right = temp;

                // 将左右子节点入队继续处理
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            return root;
        }
    }

    // 对称二叉树 https://leetcode.cn/problems/symmetric-tree/?envType=study-plan-v2&envId=top-100-liked
    public boolean isSymmetric(TreeNode root) {
        // 符合要求即
        // （1）左子树的右子树 = 右子树的左子树
        // （2）左子树的左子树 = 右子树的右子树
        // 两个指针同时遍历左右子树，【🌟递归 --> 转换为单个节点问题 】检查
        return check(root.left, root.right);
    }

    // 检查是否相同
    private boolean check(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            // 同时为空
            return true;
        }
        if (left == null || right == null) {
            // 只有一个为空
            return false;
        }
        return left.val == right.val && check(left.left, right.right) && check(left.right, right.left);
    }


    // 二叉树的层序遍历 https://leetcode.cn/problems/binary-tree-level-order-traversal/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        // 使用队列，当前层依次出队加入队列，直到队列为空
        Queue<TreeNode> queue = new ArrayDeque();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 进入依次循环遍历一层
            List<Integer> levelResult = new ArrayList<>();
            int currentLevelSize = queue.size();  // 因为要一层一个List输出，所以需要一个额外的变量来记录当前层遍历完了，如果直接一个list输出，则不需要这个
            while (currentLevelSize > 0) {        // 也是因为要一层层输出。所以要两个循环，否则直接出队的时候加上左右节点 一个循环就搞定了
                TreeNode targetNode = queue.poll();
                levelResult.add(targetNode.val);
                if (targetNode.left != null) queue.offer(targetNode.left);
                if (targetNode.right != null) queue.offer(targetNode.right);
                currentLevelSize--;
            }
            result.add(levelResult);
        }
        return result;
    }


    // 将有序数组转换为二叉搜索树 https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/description/?envType=study-plan-v2&envId=top-100-liked
    public TreeNode sortedArrayToBST(int[] nums) {
        // 因为要平衡，所以要先sort，但是因为给的是升序数组，所以不用了
        if (nums == null || nums.length == 0) { return null; }
        // 【🌟直接找中间节点作为根节点】
        int middleIndex = nums.length / 2;
        TreeNode rootNode = new TreeNode(nums[middleIndex]);
        // 左右子树都是二叉搜索树，直接【🌟递归】
        rootNode.left = sortedArrayToBST(Arrays.copyOfRange(nums, 0, middleIndex));     // Arrays.copyOfRange
        if (middleIndex + 1 <= nums.length - 1) {
            rootNode.right = sortedArrayToBST(Arrays.copyOfRange(nums, middleIndex + 1, nums.length)); // 因为to的index是不包含的 所以是nums.length
        }
        return rootNode;
    }

    // 验证二叉搜索树 https://leetcode.cn/problems/validate-binary-search-tree/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean isValidBST(TreeNode root) {
        // ❌ 递归
        // 思想：递归检查 —— 左子树里可能有比根节点还大的值 因为只是比较了左节点，而不是整个左子树
//        if (root == null) return true;  // 空节点也是二叉搜索树
//        boolean currentResult = (root.left == null ? true : (root.val > root.left.val))
//                && (root.right == null ? true : (root.val < root.right.val));
//        if (!currentResult) {
//            return false;
//        }
//        // 子树检查
//        boolean leftResult = isValidBST(root.left);
//        if (!leftResult) {
//            return false;
//        }
//        boolean rightResult = isValidBST(root.right);
//        if (!rightResult) {
//            return false;
//        }
//        return true;

        // 法1: 思想：【🌟 递归传递上下界】
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);  // 注意用long，避免节点刚好是Integer的极值
        // 法2: 中序遍历，遍历结果判是不是递增的
    }
    private boolean isValidBST(TreeNode root, long minValue, long maxValue) {
        if (root == null) return true;  // 空节点也是二叉搜索树
        // 检查当前节点
        if (root.val <= minValue || root.val >= maxValue) return false;
        // 递归左右子树 【🌟左子树更新一个上限，下限和root共用； 右子树更新一个下限，上限和root共用】
        return isValidBST(root.left, minValue, root.val) && isValidBST(root.right, root.val, maxValue);
    }



    // 二叉树的右视图  https://leetcode.cn/problems/binary-tree-right-side-view/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> rightSideView(TreeNode root) {
        // 法1: 层序遍历，然后取每一层的最后一个值
//        List<Integer> result = new ArrayList<>();
//        if (root == null) return result;
//        List<List<Integer>> levelTraversal = levelOrder(root);
//        for (List<Integer> levelList : levelTraversal) {
//            result.add(levelList.getLast());
//        }
//        return result;

        // 法2: 深度优先遍历递归，按【🌟中右左🌟】的顺序遍历
        // 可以 【🌟🌟确保每层遍历都是右边的先被访问到】，然后当 【🌟层数和当前size相等】的时候就加入（表示最右边的值）
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        dfsRightSideView(root, 0, result);
        return result;
    }
    private void dfsRightSideView(TreeNode root, int level, List<Integer> result) {
        if (root == null) return;
        if (level == result.size()) { //  🌟此时表示当前值（最右边的节点）还没有被加入
            result.add(root.val);
        }
        dfsRightSideView(root.right, level + 1, result); // 这里先遍历右子树确保了先访问右边的
        dfsRightSideView(root.left, level + 1, result);
    }


    // 二叉树展开为链表 https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
    public void flatten(TreeNode root) {
        // 前序遍历顺序：中左右
        // 读出各个节点来之后，依次处理
        if (root == null) return;
        // （1）前序访问root 【🌟先构建访问结构（List）】
        List<TreeNode> preOrderList = new ArrayList<>();
        preOrderFlattenVisit(root, preOrderList);
        // （2）【然后再 依次处理左右指向】
        for (int i = 0; i < preOrderList.size(); i++) {
            TreeNode node = preOrderList.get(i);
            node.left = null;
            if (i + 1 < preOrderList.size()) {
                node.right = preOrderList.get(i + 1); // right指向前序遍历的下一个
            } else {
                node.right = null; // 最后一个单独设置
            }
        }
    }

    private void preOrderFlattenVisit(TreeNode node, List<TreeNode> sourceList) {
        if (node == null) return;
        sourceList.add(node);
        preOrderFlattenVisit(node.left, sourceList);
        preOrderFlattenVisit(node.right, sourceList);
    }




    // 从前序与中序遍历序列构造二叉树 https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/description/?envType=study-plan-v2&envId=top-100-liked
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 前序遍历是 中 左 右
        // 中序遍历是 左 中 右
        // 1. 前序遍历的第一个元素是根节点      ---- > 【🌟前序找根节点】
        // 2. 在中序遍历中找到根节点的位置      -----> 【🌟中序根据根节点确认左右子树】
        // 3. 根节点左边是左子树，右边是右子树
        // 4. 递归构造左右子树
        if (preorder.length == 0 || inorder.length == 0) { return null; }

        // 🌟使用hashMap存储中序遍历的节点，从而快速找到中序遍历中根节点的下标（key: inorder[i], value: i）
        Map<Integer, Integer> inOrderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inOrderMap.put(inorder[i], i);
        }
        return buildTreeRecursive(preorder,
                0,
                preorder.length - 1,
                inorder,
                0,
                inorder.length - 1,
                inOrderMap
                );
    }

    private TreeNode buildTreeRecursive(int[] preorder,    // 双指针传递子数组区间
                                        int subPreStart,
                                        int subPreEnd,
                                        int[] inOrder,
                                        int subInStart,
                                        int subInEnd,
                                        Map<Integer, Integer> inOrderMap // 传递Map，从而无需重建
    ) {
        if (subPreStart > subPreEnd || subPreEnd >= preorder.length
                || subInStart > subInEnd || subInEnd >= inOrder.length) {
            return null;
        }
        // （1）根据前序遍历找到根节点
        int rootValue = preorder[subPreStart];
        // （2）获取根节点在中序的位置
        int rootIndexInOrder = inOrderMap.get(rootValue);
        // （3）根据中序位置找出左子树位置
        int leftSize = rootIndexInOrder - subInStart;
        // （4）构建
        TreeNode root = new TreeNode(rootValue);
        root.left = buildTreeRecursive(preorder,
                subPreStart + 1,
                (subPreStart + 1) + (leftSize - 1),  // 这里要理解下 ，(subPreStart + 1) 是起点 ， (leftSize - 1) 是size向下标的转换
                inOrder,
                subInStart,
                rootIndexInOrder - 1,
                inOrderMap
                );
        root.right = buildTreeRecursive(preorder,
                subPreStart + leftSize + 1,
                subPreEnd,
                inOrder,
                rootIndexInOrder + 1,
                subInEnd,
                inOrderMap
                );
        return root;
    }



    // 路径总和 III https://leetcode.cn/problems/path-sum-iii/description/?envType=study-plan-v2&envId=top-100-liked
    public int pathSum(TreeNode root, int targetSum) {
        // 【🌟双层递归】 （时间O（n * n）空间 O（h））
        // 1. 外层递归：遍历每个节点，作为路径的起点
        // 2. 内层递归：从当前节点开始，计算所有向下的满足条件的路径和
        int result = 0;
        if (root == null) return result;

        // ✅ 修复：将targetSum转换为long，避免溢出
        result = fullFillCountPath(root, (long)targetSum);

        result += pathSum(root.left, targetSum);
        result += pathSum(root.right, targetSum);

        return result;
    }

    // 计算【以当前root为起点】满足条件的条数
    private int fullFillCountPath(TreeNode root, long targetSum) {
        if (root == null) return 0;

        int result = 0;

        if ((long)root.val == targetSum) {
            result++;
        }

        result += fullFillCountPath(root.left, targetSum - (long)root.val);  // 🌟 变化targetSum目标查找值 🌟
        result += fullFillCountPath(root.right, targetSum - (long)root.val);

        return result;
    }







    // 二叉树的最近公共祖先 https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/?envType=study-plan-v2&envId=top-100-liked
    private TreeNode targetCommonAncestor = null;
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 定义个根节点成员变量记录最近的根节点
        // 递归找p/q，找的过程中对targetCommonAncestor赋值
        targetCommonAncestor = null;
        dfsFind(root, p, q);
        return targetCommonAncestor;
    }

    // 找当前树上是否包含了p或者q
    private boolean dfsFind(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return false;
        // 找左子树和右子树 是否有符合条件的值
        boolean leftFound = dfsFind(root.left, p, q);
        boolean rightFound = dfsFind(root.right, p, q);


        // 情况1:🌟 分别在左子树和右子树
        if (leftFound && rightFound) {
            targetCommonAncestor = root;
        }
        // 情况2：🌟某一个节点自身就是公共祖先，另一个在他的子树上
        else if ((root == p || root == q) && (leftFound || rightFound)) {
            targetCommonAncestor = root;
        }

        // 以上两种情况找到的一定是最近的公共祖先，
        // 因为是dfs 从下面向上找的，再往上的节点不会满足上述两种场景

        return leftFound        // 当前左子树符合条件
                || rightFound
                || root == p    // 当前值就是某一个
                || root == q;
    }








    // 延伸：判断一个二叉树是不是平衡二叉树

    // 平衡二叉树（Balanced Binary Tree），核心定义是：任意节点的左右子树高度差绝对值不超过 1。
//    推荐解法：【自底向上（🌟后序遍历）】
//    这种方法的时间复杂度是 O(N)。
//    核心思路
//    我们不想重复计算高度。我们希望在递归“归来”的过程中（后序遍历），顺便把子树的高度带上来（所以不用自顶向上，避免子树重复计算）。
//    如果发现某个子树已经不平衡了，直接向上返回一个特殊标记（比如 -1），表示“已经挂了，不用再算了”。
        public boolean isBalanced(TreeNode root) {
            // 如果返回 -1，说明不平衡；否则说明平衡
            return recur(root) != -1;
        }

        // 这个递归函数有两个作用：
        // 1. 如果子树平衡，返回子树的实际高度（>= 0）
        // 2. 如果子树不平衡，返回 -1
        private int recur(TreeNode node) {
            // 1. 终止条件：空节点是平衡的，高度为 0
            if (node == null) return 0;

            // 2. 先判断左子树
            int leftHeight = recur(node.left);
            if (leftHeight == -1) return -1; // 剪枝：左边已经不平衡了，直接向上报错

            // 3. 再判断右子树
            int rightHeight = recur(node.right);
            if (rightHeight == -1) return -1; // 剪枝：右边已经不平衡了，直接向上报错

            // 4. （后序🌟）判断当前节点是否平衡
            if (Math.abs(leftHeight - rightHeight) > 1) {
                return -1; // 此时此刻，左右高度差超过1，标记为不平衡
            }

            // 5. 如果平衡，返回当前节点的高度（左右最大值 + 1）
            return Math.max(leftHeight, rightHeight) + 1;
        }


        // 延伸： 判断一颗二叉树是不是完全二叉树

    // 完全二叉树的特点是：节点必须按照从上到下、从左到右的顺序紧凑排列，中间不能有空隙。

    // 思路：【 🌟 BFS（层序遍历）】

    //核心逻辑：开关法
    //完全二叉树的特点是：节点必须按照从上到下、从左到右的顺序紧凑排列，中间不能有空隙。
    //我们可以利用层序遍历的特点：
    // 我们准备一个布尔变量 isEnd（或者叫 foundNull），初始为 false。
    // 当我们遍历到一个 空节点 (null) 时，打开开关 isEnd = true。这意味着：“从此以后，队列里剩下的所有节点都必须是 null，不能再出现活人（非空节点）了”。
    // 如果开关打开后，我们又遇到了一个 非空节点，说明树中间有“断层”或者“空洞”，直接返回 false。

    public boolean isCompleteTree(TreeNode root) {
        if (root == null) return true;

        // 使用 LinkedList 因为它允许插入 null 元素（ArrayDeque 不允许）
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        // 开关：是否已经遇到了空节点
        boolean isEnd = false;

        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();

            if (cur == null) {
                // 第一次遇到空节点，开启“警戒模式”
                // 这意味着之后遍历到的所有节点都必须是 null
                isEnd = true;
            } else {
                // 如果当前节点不是 null
                if (isEnd) {
                    // 如果在“警戒模式”下发现了非空节点，说明不连续，有空洞
                    return false;
                }

                // 正常入队，不管孩子是不是 null 都入队
                queue.offer(cur.left);
                queue.offer(cur.right);
            }
        }

        return true;
    }
}
