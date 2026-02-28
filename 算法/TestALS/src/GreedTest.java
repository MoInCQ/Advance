import java.util.*;

public class GreedTest {


//    贪心算法（Greedy Algorithm）的核心思想可以用一句话概括：
//            “只顾眼前利益，不看长远未来。”
//
//    什么是贪心？
    //    贪心算法在解决问题时，不从整体最优上加以考虑，而是每一步都做出在当前看来是最好的选择（局部最优解）。
    //    它寄希望于：如果你每一步都选最好的，最后的结果也就是最好的。
    //    优点：简单、快（通常是 O(N) 或 O(N log N)）。
    //    缺点：鼠目寸光。很多时候，“步步最优”不等于“全局最优”。

    public static void main(String[] args) {

    }


    // 买卖股票的最佳时机 https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/description/?envType=study-plan-v2&envId=top-100-liked
    public int maxProfit(int[] prices) {
        // 贪心，假设每天都卖，取这些天中利润最大值即可
        // 🌟假设我们在第 i 天卖出股票得到了最大的利润，那我们的买入价格一定是 前i-1天 中最低价格时买入的


//        这个贪心之所以成立，是因为：
//        “最大利润”这个全局问题，可以分解为 N 个子问题：
//        如果在第 1 天卖，最大利润是多少？
//        如果在第 2 天卖，最大利润是多少？
//...
//        如果在第 N 天卖，最大利润是多少？
//        这 N 个子问题覆盖了所有可能性。而我们的贪心算法，就是用 O(1) 的代价极其高效地解决了每一个子问题，然后取了其中的最大值。
//        这就是为什么局部最优（每天的最佳卖出策略）能推导出全局最优（整个时间段的最佳卖出策略）。

        int minBuyPrice = Integer.MAX_VALUE;
        int result = 0;        // 待选值
        for (int price : prices) {
            if (price < minBuyPrice) {
                // 贪心1: 有更低的成本，就维护更低的成本（找到卖出日 i 的 前 i - 1天最低的成本）
                minBuyPrice = price;
            } else if (price - minBuyPrice > result) {                           // 核心：🌟每天都卖，找最大
                // 贪心2: 如果当前卖比之前的 最大收益 还要收益的多，那就卖掉，更新最大收益
                result = price - minBuyPrice;
            }
        }
        return result;
    }


    // 跳跃游戏 https://leetcode.cn/problems/jump-game/?envType=study-plan-v2&envId=top-100-liked
    public boolean canJump(int[] nums) {
        // 贪心（因为不关心具体的路线），问题转化为，🌟在当前步可以覆盖的最远范围（每一步都跳最远），如果能覆盖到 nums.length - 1，则返回true
        // 因为是遍历找能覆盖之后的最远范围，所以是满足条件的

//        为什么这是贪心？
    //        我们不关心具体的路线（是 0->1->4 还是 0->2->4）。
    //        我们只关心“能量”（覆盖范围）。
    //        每到一个点，我们都贪心地吸取这个点的能量，试图让我们的覆盖范围变得更大。
    //        只要覆盖范围能罩住终点，就成功。


        if (nums == null || nums.length == 0) {
            return true;
        }
        int maxStep = nums[0];                  // 表示当前可到的最远的下标
        for (int i = 1; i < nums.length; i++) {
            if (i > maxStep) {
                // 🌟当前的位置已经大于前面的元素可以覆盖的最大步数
                return false;
            } else if (maxStep >= nums.length - 1) {
                // 🌟已经可以覆盖到最后一步了，即满足条件
                return true;
            }
            maxStep = Math.max(maxStep, i + nums[i]);   // 🌟 i + nums[i]来更新maxStep   表示已经在当前i位置，当前位置可跳的最远距离
        }
        return true;   // 能全遍历完也说明可以到达最后一个
    }


    public int jump(int[] nums) {
        // 法1: 贪心解法
        // 查找每一步可以到达的最远范围（🌟curMaxEnd），当到达这个最远范围的时候  说明一定要再找一步跳 step++
        // 在往最远范围走的过程中，寻找下一个可达的最远范围（🌟nextMaxEnd）
        int step = 0;
        if (nums == null || nums.length <= 1) {
            return step;
        }
        // 构建初始
        int curMaxEnd = 0;
        int nextMaxEnd = 0;
        for (int i = 0; i < nums.length; i++) {
            // 【在遍历当前可以跳的过程中 🌟🌟】更新当前步数的下一跳可以到达的最远范围
            nextMaxEnd = Math.max(nextMaxEnd, i + nums[i]);
            if (curMaxEnd == i) {
                // 当前这一步走到最远了，要在当前的范围内选一步再跳，所以++
                step++;
                if (nextMaxEnd >= nums.length - 1) {      // 如果已经可以到最远了 说明选中跳的这一步已经可以符合条件了
                    break;
                }
                curMaxEnd = nextMaxEnd;                   // 🌟代表选中了当前范围内可以跳最远的那一步，更新当前值
            }
        }

        return step;




        // 法2: dp 解法

//            int n = nums.length;
//            // 定义 dp 数组：到达索引 i 的最小步数
//            int[] dp = new int[n];
//
//            // 初始化为一个比最大可能步数还大的值（相当于无穷大）
//            // 因为最多跳 n-1 步，所以 n 就够了
//            Arrays.fill(dp, n);
//
//            // 起点不需要跳，步数为0
//            dp[0] = 0;
//
//            // 外层循环：填表，从第 1 格算到第 n-1 格
//            for (int i = 1; i < n; i++) {
//                // 内层循环：往回找，看谁能跳到 i
//                for (int j = 0; j < i; j++) {
//                    // 如果位置 j 能跳到位置 i
//                    if (j + nums[j] >= i) {
//                       // 更新到达 i 的最小步数
//                        //   想要到达 i，我必须从前面的某个位置 j 跳过来。
//                        //   只要 j 能跳到 i（即 j + nums[j] >= i），那么 dp[i] 就可以是 dp[j] + 1。
//                           // 我们要找所有能跳到 i 的 j 里面，步数最少的那个。
//                          // dp[i] = min(dp[j] + 1)，其中 0 <= j < i 且 j + nums[j] >= i。
//                        dp[i] = Math.min(dp[i], dp[j] + 1);
//                    }
//                }
//            }
//
//            return dp[n - 1];

    }



    // 划分字母区间 https://leetcode.cn/problems/partition-labels/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> partitionLabels(String s) {
        // 根据题意 “同一字母最多出现在一个片段中”  即--> 【🌟找到当前子串中 所有元素的最大的index 进行切割，获得满足条件的子串】
        // 建立一个map，key是 26个字母 ， value是当前这个字母出现的最远的距离，从而可以进行O（1）时间的比较

        List<Integer> result = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return result;
        }

        // 1、先遍历一遍，构建 【每个字符出现过的最远的index map🌟】
        HashMap<Character, Integer> maxIndexMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
//            if (maxIndexMap.containsKey(c)) {
//                maxIndexMap.put(c, Math.max(maxIndexMap.get(c), i));   // 可以省略，因为正着遍历，后面的index一定比前面的大
//            } else {
                maxIndexMap.put(c, i);
//            }
        }

        // 2、开始切割，根据出现过的字母和当前最远的距离来比较，如果遍历到达了当前最远的距离，则可以切割了

        // 贪心的体现
//        我们在每一步更新 end 时，都在做一个贪心决策：
//        “【为了让当前这个片段合法，我不得不把边界扩展到这么远。🌟】”
//        我们总是试图在最早的合法位置切断（题目要求“尽可能多的片段”），只要条件满足（i == end），立马落刀，绝不拖泥带水。

        int curStartIndex = 0;                      // 起始点
        int curEndIndex = 0;                        // 🌟待查找的终点
        for (int i = 0; i < s.length(); i++) {      // 🌟i是游标
            curEndIndex = Math.max(curEndIndex, maxIndexMap.get(s.charAt(i)));
            if (i == curEndIndex) {
                // 可以安全切割的子串
                result.add(curEndIndex - curStartIndex + 1);        // +1 是因为求的是植树问题中的 “树” 的个数
                // 更新下一个起点
                curStartIndex = i + 1;
            }
        }
        return result;
    }
}
