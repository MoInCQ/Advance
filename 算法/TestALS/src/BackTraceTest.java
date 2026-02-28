import java.time.temporal.ChronoField;
import java.util.*;
public class BackTraceTest {
    // 回溯的思想
//    🌟核心思想：决策树的遍历 ---> 画图
//      你可以把回溯算法理解为在决策树上游走的过程。它本质上是一种深度优先搜索 (DFS)。
//
//    想象你在走迷宫：
//      前进：你站在一个岔路口（决策点），选择一条路走下去（做选择）。
//      触底/撞墙：
//          如果走到了终点（满足结束条件），就记录下这条路径。
//          如果走进了死胡同（不满足条件），就退回到上一个岔路口。
//      撤销（回溯）：当你回到岔路口时，你需要把刚才“走过这条路”的记忆抹去（撤销选择），这样你才能以干净的状态去选择下一条岔路。
//
//    三个关键名词：
//      （1）路径 (Path)：已经做出的选择，通常用一个列表/数组记录。
//      （2）选择列表 (Selection List)：当前节点还可以做出的选择。
//      （3）结束条件 (Ending Condition)：到达决策树底层，无法再做选择，或者已经找到答案。


    // 🌟 回溯问题通用模版
    //result = []
    //def backtrack(路径, 选择列表):
    //    if 满足结束条件:
    //        result.add(路径)
    //        return
    //
    //    for 选择 in 选择列表:
    //        # 1. 剪枝（可选）：如果当前选择不符合要求，跳过
    //        if 不符合要求: continue
    //
    //        # 2. 做选择
    //        将该选择加入路径
    //        将该选择从选择列表中移除 (或标记为已访问)
    //
    //        # 3. 进入下一层决策树
    //        backtrack(路径, 选择列表)
    //
    //        # 4. 撤销选择 (关键步骤！)
    //        将该选择从路径中移除
    //        将该选择恢复到选择列表中 (或标记为未访问)


    public static void main(String[] args) {
        BackTraceTest instance = new BackTraceTest();

        int[] input = {1, 2, 3};
        List<List<Integer>> result = instance.subsets(input);
    }

    // 全排列 https://leetcode.cn/problems/permutations/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }

        // 【🌟路径 集合 —— 一直用】
        LinkedList<Integer> path = new LinkedList<>();
        // 【🌟待选值 集合 —— 一直用】 与nums同规模，标记对应的nums的index有没有被访问过
        boolean[] used = new boolean[nums.length];

        // 开始回溯
        backTracePermute(result, path, nums, used);

        return result;
    }

    private void backTracePermute(List<List<Integer>> result,
                                  LinkedList<Integer> path,
                                  int[] nums,
                                  boolean[] used) {
        // 1、【结束回溯】到达叶子节点，符合条件，
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));         // 🌟 注意这里新建一个path结果【固化下path结果】 ，避免因为引用传递导致path被回溯删除掉
            return;
        }
        for (int i = 0; i < nums.length; i++) {

            // 2、【🌟剪枝】，去除已访问过的尝试 —— 如果不剪枝 就会造成元素重复使用 & 结束回溯的条件也不能再通过path.size()判断
            if (used[i]) {
                continue;
            }

            // 3、符合条件加入路径
            path.add(nums[i]);
            used[i] = true;    // 标记已访问

            // 4、【🌟dfs递归寻找一下个待选值加入路径】
            backTracePermute(result, path, nums, used);

            // 5、【🌟回溯恢复状态】
            path.removeLast();    // 去除当前路径
            used[i] = false;      // 恢复未访问状态
        }
    }



    // 子集 https://leetcode.cn/problems/subsets/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> subsets(int[] nums) {
        // 思路：变更结束路径选择的条件
        // 🌟画一下递归路径树
//        输入：nums = [1,2,3]
//        输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
        //         null
        //    1      2      3
        //  2   3   3
        // 3


        // 全排列：关注顺序，需要 【🌟 used 数组】，每次从头遍历。
        // 子集/组合：不关注顺序（[1,2] 等于 [2,1]），需要【🌟 startIndex 🌟】 来控制“不回头”。

//        选了 1 之后，下一层只能选 2, 3。
//        选了 2 之后，下一层只能选 3（不能回头选 1，因为 [1, 2] 已经在左边分支包含了）。
//        树上的每一个节点（包括根节点）都要加入结果集。

        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        // 路径
        LinkedList<Integer> pathList = new LinkedList<>();

        // 开始回溯遍历
        backTraceSubsets(result, pathList, nums, 0);   // 此时endDepth是最深的叶子节点，这样才能确保递归进行下去

        return result;
    }

    private void backTraceSubsets(List<List<Integer>> result,
                                  LinkedList<Integer> pathList,
                                  int[] nums,
                                  int startIndex) {
        // 每个节点都要进结果集
        result.add(new ArrayList<>(pathList));

        // 结束条件
        if (startIndex >= nums.length) {
            return;
        }

        // 以 startIndex 开始找后面的可能值
        for (int i = startIndex; i < nums.length; i++) {
            // 选择
            pathList.add(nums[i]);
            // dfs递归
            backTraceSubsets(result, pathList, nums, i + 1);
            // 回溯恢复状态
            pathList.removeLast();
        }

    }


    // 电话号码的字母组合  https://leetcode.cn/problems/letter-combinations-of-a-phone-number/description/?envType=study-plan-v2&envId=top-100-liked
    public List<String> letterCombinations(String digits) {
        // 🌟画路径树 每一层的待选值都不一样 要从map中取
        List<String> result = new ArrayList<>();
        if (digits == null || digits.length() == 0) {
            return result;
        }
        // 构建 数字-符号 map
        HashMap<Character, List<Character>> map = new HashMap<>();
        map.put('2', Arrays.asList('a', 'b', 'c'));
        map.put('3', Arrays.asList('d', 'e', 'f'));
        map.put('4', Arrays.asList('g', 'h', 'i'));
        map.put('5', Arrays.asList('j', 'k', 'l'));
        map.put('6', Arrays.asList('m', 'n', 'o'));
        map.put('7', Arrays.asList('p', 'q', 'r', 's'));
        map.put('8', Arrays.asList('t', 'u', 'v'));
        map.put('9', Arrays.asList('w', 'x', 'y', 'z'));

        // 路径列表
        LinkedList<Character> pathList = new LinkedList<>();
        // 待选值: 目标数字字符串的数组
        char[] selectArr = digits.toCharArray();

        // dfs回溯遍历
        backTraceLetterCombinations(result, selectArr, pathList, map,0);

        return result;
    }

    private void backTraceLetterCombinations(List<String> result,
                                             char[] selectArr,
                                             LinkedList<Character> pathList,
                                             HashMap<Character, List<Character>> map,
                                             int selectIndex) {
        if (pathList.size() == selectArr.length) {
            StringBuffer sb = new StringBuffer();
            for (char c : pathList) {
                sb.append(c);
            }
            result.add(sb.toString());
            return;
        }

        if (selectIndex >= selectArr.length) {
            return;
        }

        // 找到这个数字所对应的字符列表 —— 新的待选值
        List<Character> charArr = map.get(selectArr[selectIndex]);
        // 开始遍历
        for (int i = 0; i < charArr.size(); i++) {
            // 选择
            pathList.add(charArr.get(i));
            // dfs查找
            backTraceLetterCombinations(result, selectArr, pathList, map, selectIndex + 1);   // 通过 + 1 找下一个数字对应的字母 构建新的待选值
            // 回溯恢复
            pathList.removeLast();
        }
    }



    // 组合总和 https://leetcode.cn/problems/combination-sum/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        // 思路：画图 —— key： 可以重复用 说明不用used数组，【🌟但是不能回头（会造成不同顺序，但重复）的序列，所以需要startIndex】
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length == 0) {
            return result;
        }
        LinkedList<Integer> pathList = new LinkedList<>();
        backTraceCombinationSum(result, pathList, candidates, target, 0);
        return result;
    }
    private  void backTraceCombinationSum(List<List<Integer>> result,
                                          LinkedList<Integer> pathList,
                                          int[] candidates,
                                          int target,
                                          int startIndex) {                    // 🌟可以重复读，但不能回头    否则会出现 [2,3,5] ， 8  =>    [[2,2,2,2],[2,3,3],[3,2,3] ❌,[3,3,2] ❌,[3,5],[5,3] ❌]
        int currentSum = sumPathList(pathList);
        if (currentSum == target) {
            // 结束 1: 符合条件
            result.add(new ArrayList<>(pathList));
            return;
        } else if (currentSum > target) {
            // 结束 2：超限
            return;
        }
        for (int i = startIndex; i < candidates.length; i++) {
//            // 剪枝优化 （替代上面的超限）
//            if (currentSum + candidates[i] > target) {
//                continue;
//            }
            pathList.add(candidates[i]);
            backTraceCombinationSum(result, pathList, candidates, target, i);  // 🌟 因为当前值可以重复 所以无需i + 1
            pathList.removeLast();
        }
    }
    private int sumPathList(LinkedList<Integer> pathList) {
        int result = 0;
        for (int i : pathList) {
            result += i;
        }
        return result;
    }


    // 括号生成
    public List<String> generateParenthesis(int n) {
        // 核心思想：🌟左括号随便加，右括号不能超过左括号

        // 输出顺序eg：
        // 输入：n = 3
        // 输出：["((()))","(()())","(())()","()(())","()()()"]
        List<String> result = new ArrayList<>();
        if (n <= 0) {
            return result;
        }
        StringBuffer path = new StringBuffer();
        backTraceGenerateParenthesis(result, path, 0, 0, n);
        return result;
    }

    private void backTraceGenerateParenthesis(List<String> result,
                                         StringBuffer path,
                                         int leftCount,
                                         int rightCount,
                                         int maxPair) {
        if (path.length() == maxPair * 2) {
            result.add(path.toString());
            return;
        }
        // key1：🌟左括号可以在合法的区间内随时加 【“合法性控制” —— 生成过程中剪枝】
        if (leftCount < maxPair) {       // 是小于，因为最后依次进入后再加一个 刚好等于 ，如果是小于等于则会多加一个 （
            path.append("(");
            backTraceGenerateParenthesis(result, path, leftCount + 1, rightCount, maxPair);
            path.deleteCharAt(path.length() - 1);
        }
        // key2：🌟右括号要配对左括号的数量 【 “选择的限制” —— 生成过程中剪枝】
        if (rightCount < leftCount) {    // 是小于，同理
            path.append(")");
            backTraceGenerateParenthesis(result, path, leftCount, rightCount + 1, maxPair);
            path.deleteCharAt(path.length() - 1);
        }
    }



    // 单词搜索 https://leetcode.cn/problems/word-search/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean exist(char[][] board, String word) {
        // 首先确定回溯路径查找
        // 然后确定要找待选值是四个方向

        // key1：🌟需要剪枝进入方向
        //  ❌ 避免死循环使用sourceDirection，初始的方向可以为-1 —— 无法解决绕了一圈绕回来的情况，还是会重复使用
        //  ✅ 使用和问题规模一样的visit数组（可以使用原数组 加特殊标记）访问过的进行标记
        // key2: 🌟无需使用pathList，递归返回布尔结果就是pathList，使用 checkIndex == word.length 确认符合条件结束

        // 时间复杂度： M * N * （3 * word.length）   因为只有三个方向，决策树最长就是word的长度
        // 空间复杂度： word.length   递归栈深度，即 word的长度

        if(board == null || word == null || board.length == 0 || word.length() == 0) {
            return false;
        }
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  // 上下左右
        // 所有值遍历回溯检查
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (backTraceExist(board, directions, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean backTraceExist(char[][] board,
                                   int[][] directions,
                                   String word,
                                   int currentRow,
                                   int currentColumn,
                                   int checkIndex) {
        if (currentRow < 0 || currentColumn < 0 || currentRow >= board.length || currentColumn >= board[0].length) {
            // 失败结束态1 ： 超限，即不满足
            return false;
        }

        char currentValue = board[currentRow][currentColumn];

        if (word.charAt(checkIndex) != currentValue) {
            // 失败结束态2 ： 当前值不符合, 直接返回false    ——   （1） 检查当前值是不是word对应位置的值 （2）🌟 如果之前used了 这里也被标记成了特殊值，不会匹配 —— 少写一个if条件判断
            return false;
        }


        // 1、选择 - 标记已读取（即used标记）
        if (checkIndex == word.length() - 1) {
            // 成功结束态：找到目标值 （🌟 此时能进行比较是因为先进行了上面 当前值是否符合的判定，所以checkIndex可以认为是符合条件的）
            return true;
        }
        board[currentRow][currentColumn] = '!';   // 标记一个特殊值，记录used

        boolean result = false;
        // 2、递归四个方向
        for (int[] direction : directions) {
            // 剪枝: 已经找到了就不用遍历了
            if (result) {
                break;
            }
            int selectRow = currentRow + direction[0];
            int selectColum = currentColumn + direction[1];
            result = backTraceExist(board, directions, word, selectRow, selectColum, checkIndex + 1);   // 🌟 这里 + 1 表示当前值符合条件，检查下一个
        }

        // 3、回溯恢复状态
        board[currentRow][currentColumn] = currentValue;

        // 返回递归结果
        return result;
    }



    // 分割回文串 https://leetcode.cn/problems/palindrome-partitioning/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<String>> partition(String s) {

        // 时间复杂度： O (s.length * s.length)
        // 空间 O (s.length)

        // 画图
        // 【待选值】是 s 的数组 “🌟切割后剩余内容的切法”，所以需要一个startIndex避免回头
        // 【结束态】每一个选择结果检查一下是否是回文
        // 难点：要所有可能的分割方案，所以 画图要画对 —— 🌟 startIndex代表的是切谁 （核心思路：把“切割线”当成“选择，想象你在切香肠（字符串 s））
//                         backtrack(startIndex=0)
//                       /           |            \
//                 切"a"             切"aa"            切"aab"
//                /     \             /
//           切"a"        切"ab"       切[b] ✅
//            /
//          切”b“✅
        List<List<String>> result = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return result;
        }
        LinkedList<String> pathList = new LinkedList<>();
        backTracePartition(s, 0, result, pathList);
        return result;
    }

    private void backTracePartition(String s,
                                    int startIndex,
                                    List<List<String>> result,
                                    LinkedList<String> pathList) {
        if (startIndex >= s.length()) {         // 依赖后面不是回文的时候 startIndex不会++ 的剪枝  或者  🌟 从语义上来说 startIndex能检查到这 说明前面都是回文的了 ，所以是一种符合条件的方案
            // 结束态，代表当前pathList里是一种切割方案
            result.add(new ArrayList<>(pathList));
            return;
        }

        // 检查待选值
        for (int i = startIndex; i < s.length(); i++) {
            if (!checkPartition(s, startIndex, i)) {                        // 🌟 这里要注意下  回文比较的start 是 startIndex：代表检查的起点，  end是 i 代表当前检查到的位置   中间的就是待检查的部分
                // 剪枝：不是回文，尝试截长一点，继续检查 [startIndex, 下一个i]
                // 🌟 如果到i到最后一个都不是回文，则startIndex一直都不会加到 >= s.length() 即结束态，这条路径就丢弃🌟
                continue;
            }
            // 选择：只有当前是回文才加入pathList，继续检查后面的是否满足条件
            pathList.add(s.substring(startIndex, i + 1));                   // 这里加一是因为substring不包括end
            backTracePartition(s, i + 1, result, pathList);       // 这里下一个startIndex（切割起始位置）是 i + 1 🌟 而不是startIndex + 1，因为是找下一个待选值
            pathList.removeLast();
        }
    }

    // 双指针检查回文
    private boolean checkPartition(String s, int start, int end) {
        boolean result = true;
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                result = false;
                break;
            }
            start++;
            end--;
        }
        return result;
    }



}
