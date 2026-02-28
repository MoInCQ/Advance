import java.util.*;

public class DynamicPlanning {
    public static void main(String args[]) {

        DynamicPlanning instance = new DynamicPlanning();


//        int result = DynamicPlanning.climbStairs(45);

//        List result = instance.generate(5);

//        int[] input = {1,2,3,1};
//        int result = instance.rob(input);

//        int result = instance.numSquares(13);

//        int[] input = {186,419,83,408};
//        int result = instance.coinChange(input, 6249);


        String s = "leetcode";
        List<String> wordDict = Arrays.asList("leet", "code");
        boolean result = instance.wordBreak(s, wordDict);

        int i = 0;
    }

    // 爬楼梯 https://leetcode.cn/problems/climbing-stairs/?envType=study-plan-v2&envId=top-100-liked
    public static int climbStairs(int n) {
//        // 构建初始值
//        if (n <= 0) {
//            return 0;
//        }
//        if (n == 1) {
//            // 跳1 只有一种
//            return 1;
//        }
//        if (n == 2) {
//            // 1+1 或 2 两种
//            return 2;
//        }
//        // 大于初值的情况 = 跳 1级（ + 1种方案） + 跳两级（ + 1种方案 ）
//        // 构建动态规划方程 ❌ f(x) = (f(x - 1) + 1)  +  (f(x - 2) + 1)
//        // ✅ 【 f(x) = f(x - 1) + f(x - 2);🌟】  不需要单独+1 即 f(x - 1)种 然后再跳一级是固定的方案数，f(x-2)只能跳两级也是固定的
//        return climbStairs(n - 1) + climbStairs(n - 2);


        // 优化：如果n = 45， 要用递归的话 这个函数要执行 2的（45 - 2）次方次
        // 所以用滚动数组(只是思想，不是真的需要数组)来改为迭代计算，从而只有o(n)复杂度
        if (n <= 2) return n;

        // 只用两个变量代替整个数组
        int prev2 = 1;  // dp[i-2]     初始值是3的时候 即 dp[1]
        int prev1 = 2;  // dp[i-1]     初始值是3的时候 即 dp[2]
        int current = 0;
        for (int i = 3; i <= n; i++) {
            current = prev1 + prev2;  // 计算当前值

            // 🌟 关键：滚动更新（为下一次做准备）
            prev2 = prev1;      // 向前移动
            prev1 = current;    // 向前移动
        }
        return current;
    }


    // 杨辉三角 https://leetcode.cn/problems/pascals-triangle/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> generate(int numRows) {
        // 左右两边直接 + 1
        // 下一行是前一行的 （-1） + （+1）
        // 杨辉三角特点：每一行的数字个数是行数
        List<List<Integer>> result = new ArrayList<>();
        // 遍历每行
        for (int i = 0; i < numRows; i++) {
            List<Integer> rowResult = new ArrayList<>();
            // 行中元素遍历（🌟注意这里是 j <= i  因为是代表这一行遍历有几个数（行数 - 1了， 所以通过 <= 补回来））
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    rowResult.add(1);
                    continue;
                }
                // 因为每一行的开始和结束都是1，所以一定有值
                rowResult.add(result.get(i - 1).get(j - 1) + result.get(i - 1).get(j));
            }
            result.add(rowResult);
        }
        return result;
    }


    // 打家劫舍 https://leetcode.cn/problems/house-robber/description/?envType=study-plan-v2&envId=top-100-liked
    public int rob(int[] nums) {
        // 设 dp[i] 为打劫该房屋的最大收益
        // dp[i] = Max(dp[i - 2] + nums[i], dp[i - 1])
        // 即 （当前这个打劫：前面两步的最大值 + 当前这个房子） 或 （当前这个不打劫：前面一步的最大值）
        int[] dp = new int[nums.length];
        if (nums.length == 0) {
            return 0;
        }
        dp[0] = nums[0];
        if (nums.length == 1) {
            return dp[0];
        }
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }
        return dp[nums.length - 1];
    }


    // 完全平方数 https://leetcode.cn/problems/perfect-squares/?envType=study-plan-v2&envId=top-100-liked
    public int numSquares(int n) {
        // 设dp[i] 为整数i需要完全平方数的最小数量
        // 那么 dp[i] =  min(dp[i],  dp[i - j * j] + 1))
        // 即去掉一个完全平方数 + 去掉的完全平方数数量（此时是需要 + 1个数字🌟）
        if (n == 0) {
            return 0;
        }
        int[] dp = new int[n + 1];                 // 多一个1是因为要 存0，    dp[i] 是 i的值  所以是i+1
        Arrays.fill(dp, Integer.MAX_VALUE);        // dp初值全部设最大，从而用于后面找最小
        dp[0] = 0;
        for (int i = 1; i <= n;i ++) {             // 遍历所有的数字构建dp[i]
            for (int j = 1; j * j <= i; j++) {     // 尝试找到最小值
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }


    // 零钱兑换 https://leetcode.cn/problems/coin-change/?envType=study-plan-v2&envId=top-100-liked
    public int coinChange(int[] coins, int amount) {
        // 设 dp[amount] 是 amount 数量下需要的最小硬币数量
        // 那么 dp[amount] = min（dp[amount], dp[amount - coins[i]] + 1）
        // 即去掉一个硬币值的 最小数量的方案 + 当前硬币 （此时需要+ 1 即当前硬币）
        if (amount < 0 || coins.length == 0) {
            return -1;
        }
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;                                              // 设一个特殊值用于后面刚好一枚硬币时的计算
        for (int i = 1; i <= amount; i++) {                     // 构建 dp[amount]                        // 遍历【🌟填表】
            for (int j = 0; j < coins.length; j++) {            // 遍历coins，看能不能构建出最小数量的方案     // 确定【🌟待选值】
                int currentValue = coins[j];
                if (i - currentValue < 0) {
                    continue;
                }
                if (dp[i] == -1 && dp[i - currentValue] != -1) {
                    dp[i] = dp[i - currentValue] + 1;                   // 看能不能找到方案【找到】
                } else if (dp[i] > 0 && dp[i - currentValue] != -1) {
                    dp[i] = Math.min(dp[i], dp[i - currentValue] + 1);  // 常规动态规划比较最小方案【找到最好】
                }
            }
        }
        return dp[amount];
    }

    // 单词拆分 https://leetcode.cn/problems/word-break/?envType=study-plan-v2&envId=top-100-liked
    public boolean wordBreak(String s, List<String> wordDict) {
        // dp[i] = true 表示s的第i个字符是否满足条件
        // 则有 dp[i] =  dp[i - targetWord.length()]  && 当前 subString 子串符合条件
        if (s == null || s.length() == 0 || wordDict.isEmpty()) {
            return false;
        }
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {                     // 遍历每个字符
            for (int j = 0; j < wordDict.size(); j++) {             // 检测每个待选值
                String targetWord = wordDict.get(j);
                int targetWordLength = targetWord.length();
                if (i - targetWordLength < 0) {
                    continue;
                }
                if (dp[i - targetWordLength] && s.substring(i - targetWordLength, i).equals(targetWord)) {
                    dp[i] = true;
                    break;            // 符合条件了就退出  不要被其他候选word值污染
                }
            }
        }
        return dp[s.length()];
    }



    // 分割等和子集 https://leetcode.cn/problems/partition-equal-subset-sum/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean canPartition(int[] nums) {
        // 目标转为找到一个能满足 （ nums的和 / 2 ）的子序列【🌟能不能凑出 target = nums的和 / 2】 --> 原数组去除子序列剩下的内容自然也是 nums的和 / 2 （即保证剩下的值肯定也是 nums/2）
        // 设🌟 dp[target] 子序列能否凑到目标 target （从0到nums/2构建）
        // 那么就存在 dp[target] =  （不添加当前nums[i]能否满足target已经能被凑出来了） ||  (添加当前nums[i],  那么需要检查 dp[target - nums[i]] 也是能被凑出来的)

        // 示例：
        //假设我们的目标 target = 5。
        //目前手里的数字（物品）是 [2, 2, 3]。
        //我们一个数字一个数字地看：
        //第一轮：手里拿到了数字 2
        //我们要更新这排格子。对于每一个金额 j，我们都在问自己两个问题：
        //不选这个 2：之前（没拿这个2的时候）能凑出 j 吗？
        //选这个 2：之前能凑出 j - 2 吗？（如果之前能凑出 3，我现在加上 2 就能凑出 5）。
        //只要上面任意一个是 true，那现在的 dp[j] 就是 true。

        int numsSum = 0;
        for (int num : nums) {
            numsSum += num;
        }
        // 目标值如果是奇数一定凑不出来，
        // 因为相等的两个子数组和一定是相同性质，那么奇数+奇数=偶数，偶数+偶数=偶数，所以一定不可能是奇数
        // 要处理下这个，确保目标值是真正的平分
        if (numsSum % 2 != 0) return false;
        // dp的终点目标值
        int targetValue = numsSum / 2;
        boolean[] dp = new boolean[targetValue + 1];
        dp[0] = true;          // 0一定能被凑出来，处理 dp[j - nums[i]] 刚好 j - nums[i] = 0 ，即一个值就能凑出来的情况

        // 检查所有能凑出target的可能
        for (int num : nums) {                              // 遍历使用每一个数字
            for (int j = targetValue; j >= num; j--) {      // 遍历所有可能能凑出来的值（结束条件：“要凑的目标值”一定要大于等于“当前用于凑的值”）
                // key: 【🌟从后往前更新】，确保是0-1背包（每个数字只用一次）
                dp[j] = dp[j] || dp[j - num];
            }
        }

        return dp[targetValue];





        //  【0-1背包】倒着遍历 （所有枚举值只能用一次） 和 【完全背包】正着遍历（所有枚举值可以用多次）的区别

        // 为什么必须“倒着算”？（最关键的一点）
        //这是为了防止同一个数字被用两次。
        //❌ 如果我们【从前往后】更新（错误示范）：
        //假设现在 dp 数组是这样的（只有 0 是 true）：
        //[T, F, F, F, F, F] (对应金额 0, 1, 2, 3, 4, 5)
        //现在来了数字 2。
        //看金额 2：需要 dp[2-2] 也就是 dp[0]。dp[0] 是 T。
        //-> 于是 dp[2] 变成了 T。
        //数组变成：[T, F, **T**, F, F, F]
        //看金额 4：需要 dp[4-2] 也就是 dp[2]。
        //-> 注意！ 这里读到的 dp[2] 是刚刚在第1步里变绿的那个 T（意味着已经用过一次 2 了）。
        //-> 于是 dp[4] 也变成了 T。
        //出事了！ 我们手里只有一个 2，结果不仅凑出了 2，还凑出了 4。这相当于把这个 2 用了两次。
        //✅ 如果我们【从后往前】更新（正确做法）：
        //还是那个数组：
        //[T, F, F, F, F, F]
        //现在来了数字 2。
        //看金额 5：看 dp[3]，是 F。-> dp[5] 还是 F。
        //看金额 4：看 dp[2]，是 F。-> dp[4] 还是 F。
        //...
        //看金额 2：看 dp[0]，是 T。-> dp[2] 变成 T。
        //结束这一轮。
        //结果是 [T, F, T, F, F, F]。
        //你看，dp[4] 并没有受影响，因为我们在算 dp[4] 的时候，dp[2] 还没有被更新，还是旧值 F。


        // “外层遍历物品，内层倒序遍历金额。”
        // 倒序是为了让当前的物品只服务于“还未被污染”的旧状态，保证每个物品只被用一次。
    }



    // 最长递增子序列 https://leetcode.cn/problems/longest-increasing-subsequence/description/?envType=study-plan-v2&envId=top-100-liked
    public int lengthOfLIS(int[] nums) {
        // 设 dp[i] 为以第i个元素作为终点的递增子序列最大长度
        // 默认dp[0] = 1
        // 开始【🌟倒着往前找】比当前nums[i] 小的值的 Max(dp[j])，找到则dp[i] = dp[j] + 1
        if (nums.length < 2) {
            return nums.length;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int result = 1;
        for (int i = 1; i < nums.length; i++) {                          // 🌟遍历
            int currentPreMax = 0;
            for (int j = i - 1; j >= 0; j--) {                          //  🌟待选
                if (nums[i] > nums[j]) {
                    currentPreMax = Math.max(currentPreMax, dp[j]);       // 从待选值中找到前面最长的值
                }
            }
            dp[i] = currentPreMax + 1;                                    // 每个元素至少自己是一个递增的子序列
            result = Math.max(result, dp[i]);
        }
        return result;         // 最大值不一定是以最后一个元素结尾的，所以不能返回dp[nums.length - 1]，而是要手动记录一个最大值
    }


    // 乘积最大子数组 https://leetcode.cn/problems/maximum-product-subarray/?envType=study-plan-v2&envId=top-100-liked
    public int maxProduct(int[] nums) {
        // 对于数组中的每个数字 nums[i]，想要构成以它结尾的最大乘积，有三种可能：
        // (1)自己独立门户：前面的都断掉，从自己开始（比如前面是 0，或者前面是很小的负数，而自己是正数）。
        // (2)接在前面的最大值后面：如果 nums[i] 是正数，这就变大。
        // (3) 🌟【负负得正】接在前面的最小值后面：如果 nums[i] 是负数，而前面有个很小的负数（比如 -100），一乘就变成巨大的正数。

        if (nums.length == 0) return 0;
        int result = nums[0];
        int endMaxValue = nums[0];    // 🌟以【当前元素】结尾的乘积最大值
        int endMinValue = nums[0];    // 🌟以【当前元素】结尾的乘积最小值
        for (int i = 1; i < nums.length; i++) {
            int lastEndMaxValue = endMaxValue;
            int lastEndMinValue = endMinValue;
            endMaxValue = Math.max(nums[i], Math.max(lastEndMaxValue * nums[i], lastEndMinValue * nums[i]));    // 因为全部用到了nums[i] 所以 （1）如果 当前i的值是0，则 endMaxValue 和 endMinValue 都会是0 （2） 保证了一定是用到了当前元素的 --> 从而保证一定是连续的
            endMinValue = Math.min(nums[i], Math.min(lastEndMaxValue * nums[i], lastEndMinValue * nums[i]));

            result = Math.max(endMaxValue, result);   // 最大值不一定是在最后 要在每次相对最大中比较
        }

        return result;
    }


    // ------------------------ 多维动态规划 ------------------------

    // 最长公共子序列  https://leetcode.cn/problems/longest-common-subsequence/description/?envType=study-plan-v2&envId=top-100-liked
    public int longestCommonSubsequence(String text1, String text2) {
        // 设dp[i][j] 表示text1 第1个字符 - 第 i 个字符 （🌟 注意是从1开始的，便于计算初始状态） 和 text2 第1个字符 - 第 [j] 个字符（🌟 注意是从1开始的，便于计算初始状态） 的最长公共子序列
        // 那么就有  text1.charAt(i - 1) == text2.charAt( j - 1)   时 （ -1 是因为charAt是从0开始的）， 🌟 dp[i][j] = dp[i-1][j-1] + 1;
        //          不相等时，🌟 dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]) , 即从（回退一个状态）前面的状态里选一个最大的 “我试着跳过 text1 的字符算一遍，再试着跳过 text2 的字符算一遍，然后取这两个结果里最好的那个”
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];  // 默认值都是0，包含了初值dp[0][0] = 0; 所以无需显式设置初值

        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {                  // case1：匹配，相等+1      // 这里的i-1是匹配第i个字符（是数组的读数特征）
                    dp[i][j] = dp[i - 1][j - 1] + 1;                                                         // 这里的i-1是用前面的内容 + 1
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);               // case2：不匹配，跳过（即和前面的 “相同最大长度” 一样）
                }
            }
        }

        return dp[text1.length()][text2.length()];


        // 不能用双指针（贪心算法），因为当两个字符不相等时，我们无法预知“牺牲”哪一个字符未来会更好。

        // 如果不是求最长 “子序列” ，而是最长连续 “子数组”时，则需要添加一个最大值来记录，而不是dp[M][N]就是最大值了
        // 解法如下
//        // 最长公共子串 (Longest Common Substring/Subarray)
//        public int longestCommonSubstring(String text1, String text2) {
//            int M = text1.length();
//            int N = text2.length();
//            int[][] dp = new int[M + 1][N + 1];
//            int maxLen = 0; // 必须用一个变量记录最大值
//
//            for (int i = 1; i <= M; ++i) {
//                for (int j = 1; j <= N; ++j) {
//                    if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
//                        // 如果相等，延续之前的长度
//                        dp[i][j] = dp[i - 1][j - 1] + 1;
//                        // 更新全局最大值
//                        maxLen = Math.max(maxLen, dp[i][j]);
//                    } else {
//                        // 【关键区别】如果不相等，连续性断裂，直接归 0
//                        dp[i][j] = 0;
//                        // 注意：这里不能写 Math.max(dp[i-1][j], ...)
//                        // 因为必须连续，断了就没了
//                    }
//                }
//            }
//            return maxLen;
//        }
    }



    // 不同路径 https://leetcode.cn/problems/unique-paths/?envType=study-plan-v2&envId=top-100-liked
    public int uniquePaths(int m, int n) {
        // dp[i][j] 设置为到达这一步的路径数
        // 因为只能往右、往下，所以 dp[0][j] = 1 ， dp[i][0] = 1  即第一行和第一列都只有一种走法
        // 则 dp[i][j] = dp[i - 1][j] + dp[i][j - 1]  ，即到达 i,j 只有两种可能 要么是前一个往下走（dp[i - 1][j]） ，要么是前一个往右走（dp[i][j - 1]）
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];      // 因为index 是0开始的 所以这里取index 需要-1
     }

     // 最小路径和 https://leetcode.cn/problems/minimum-path-sum/description/?envType=study-plan-v2&envId=top-100-liked
     public int minPathSum(int[][] grid) {
        // 设 dp[i][j] 为 到达grid该位置的最小路径和
         // 因为只能向右或者向下，所以 dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j]
         int rows = grid.length;
         int columns = grid[0].length;
         if (rows == 0 || columns == 0) {
             return 0;
         }
         int[][] dp = new int[rows][columns];
         // 🌟 起始值单独设置
         dp[0][0] = grid[0][0];
         // 🌟 第一行的和第一列的单独设置一下
         for (int i = 1; i < columns; i++) {
             dp[0][i] = dp[0][i - 1] + grid[0][i];
         }
         for (int i = 1; i < rows; i++) {
             dp[i][0] = dp[i - 1][0] + grid[i][0];
         }
         // dp设置
         for (int i = 1; i < rows; i++) {
             for (int j = 1; j < columns; j++) {
                 dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
             }
         }
         return dp[rows - 1][columns - 1];
     }


     // 最长回文子串 https://leetcode.cn/problems/longest-palindromic-substring/?envType=study-plan-v2&envId=top-100-liked
    public String longestPalindrome(String s) {
        // 使用【中心往两边扩散】查找
        // 依次【遍历s的每一个字符，扩散找最大回文长度】，如果找到则与当前最大长度比较，保留一个最大值并返回
        // key：注意回文中心是刚好在中间那个字符（奇数个元素）还是在两个元素之间（偶数个元素）
        if (s.length() < 2) {
            return s;
        }
        int longestTargetStart = 0;
        int longestTargetEnd = 0;
        for (int i = 0; i < s.length(); i++) {
            int currentCenterExpandLength = Math.max(
                    findLongestCenterExpandLength(i, i, s),                     // 奇数长度回文的查找case 🌟🌟   // 1. 情况一：以一个字符为中心 (如 "aba")
                    findLongestCenterExpandLength(i, i + 1, s)             // 偶数长度回文的查找case 🌟🌟   // 2. 情况二：以两个字符之间的空隙为中心 (如 "abba")
                    );
            if (currentCenterExpandLength > longestTargetEnd - longestTargetStart + 1) {
                longestTargetStart = i - (currentCenterExpandLength - 1) / 2;  // 🌟 这里是中心值除法公式（int除法是向下取整），因为如果字符数量是偶数，那么起始中心值是偏左的，所以需要少减一点
                                                                                //                关于 Start:
                                                                                //                  对于奇数 len，(len-1)/2 和 len/2 结果一样。
                                                                                //                  对于偶数 len，我们需要少减一点（因为 i 偏左），(len-1)/2 恰好比 len/2 少 1，完美符合要求。
                                                                                //                关于 End:
                                                                                //                   对于奇数 len，len/2 向下取整，正好覆盖右半径。
                                                                                //                   对于偶数 len，len/2 正好覆盖右半边（包含右中心）。

                longestTargetEnd = i + currentCenterExpandLength / 2;
            }
        }
        return s.substring(longestTargetStart, longestTargetEnd + 1);  // 这里需要 + 1是因为subString是左闭右开
    }

    // 找一下以centerIndex （start / end 为中心）为中心开始扩散的最长回文长度（子序列元素个数）
    private int findLongestCenterExpandLength(int start, int end, String s) {
        while (start >= 0 && end < s.length() && s.charAt(end) == s.charAt(start)) {
            start--;
            end++;
        }
        // 此时已经越界了，所以要回退之前符合条件的状态 (end - 1) 和 (start + 1)
        return (end - 1) - (start + 1) + 1;             //  🌟 最后要多 + 1 即把自己加上 （植树问题，减法求的是树之间的距离， +1 是求范围内有多少颗树）
    }


    // 编辑距离 https://leetcode.cn/problems/edit-distance/?envType=study-plan-v2&envId=top-100-liked
    public int minDistance(String word1, String word2) {
//        1. 状态定义 dp[i][j]
//        dp[i][j] 表示：将 word1 的前 i 个字符转换成 word2 的前 j 个字符，所需要的 最少操作数。

//        2. 状态转移方程推导（核心）
//        我们要计算 dp[i][j]，还是要看 word1 的第 i 个字符（c1）和 word2 的第 j 个字符（c2）是否相等。
//          情况一：c1 == c2 （当前字符相等）
//              既然这两个字符一样，那就不需要任何操作。
//              大家直接“退一步”，继承之前的操作数即可。
//           $$dp[i][j] = dp[i-1][j-1]$$   🌟
//                (这就像 LCS 里的匹配成功)
//          情况二：c1 != c2 （当前字符不相等）
//              如果不相等，我们需要对 word1 的末尾字符进行一次操作来修补这个差异。因为我们要找最少操作数，所以要在以下三种操作中取最小值 Math.min，然后 +1（表示本次操作）。
//              这里是理解的难点，这三个方向分别代表什么操作？
//              （1）替换 (Replace)：
//                   我们把 word1 的 c1 直接替换成 c2。
//                   这样它俩就匹配了，问题转化成“word1 前 i-1 个”和“word2 前 j-1 个”的距离。
//                   对应状态：dp[i-1][j-1]。
//                   代价：dp[i-1][j-1] + 1。 🌟
//              （2）删除 (Delete)：
//                   我们把 word1 的 c1 删掉。
//                   那么 word1 就变短了，问题转化成“word1 前 i-1 个”和“word2 前 j 个” 的操作距离。 （🌟删除 = 既然 word1 现在的字符多余，我就通过一步删除操作解决它，然后把剩下的烂摊子交给 dp[i-1][j] 去解决。）
//                   对应状态：dp[i-1][j]（上面）。
//                   代价：dp[i-1][j] + 1。 🌟🌟
//              （3）插入 (Insert)：
//                   我们在 word1 的末尾插入一个 c2。
//                   这样 word1 的末尾就和 c2 匹配上了。既然匹配上了，word2 的 j 就搞定了，但 word1 原来的 i 还没动。 （🌟插入 = 既然 word1 缺一个字符来匹配 word2 的当前字符，我就花一步代价造一个出来匹配掉 word2 的当前位，然后把 word1 没处理完的部分和 word2 剩下的部分交给 dp[i][j-1] 去解决。）
//                   问题转化成“word1 前 i 个”和“word2 前 j-1 个”的距离。
//                   对应状态：dp[i][j-1]（左边）。
//                   代价：dp[i][j-1] + 1。 🌟🌟

//        3. 初始化 (Base Case) —— 便于 -1的计算 （🌟二维数组涉及 - 1的操作，先初始化第一行和第一列
//         （1）第一列 dp[i][0]：
//              把 word1 的前 i 个字符变成空字符串 word2（长度0）。
//              显然需要删除 i 次。
//              所以 dp[i][0] = i。
//         （2）第一行 dp[0][j]：
//              把空字符串 word1 变成 word2 的前 j 个字符。
//              显然需要插入 j 次。
//              所以 dp[0][j] = j。

         // 4、构建dp二维数组

        int m = word1.length();  // 行
        int n = word2.length();  // 列
        int[][] dp = new int[m + 1][n + 1];


        // 初始化第一列和第一行
        dp[0][0] = 0;       // 都是空字符串，不用操作
        for (int i = 1; i <= m; i++) {
            dp[i][0] = i;   // 第一列，需要删除i次
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = j;   // 第一行，需要插入i次
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];  // case 1 匹配成功  直接继承，不需要增加步数
                } else {
                                                  //                 字符不等，取三种操作的最小值 + 1
                    dp[i][j] = Math.min(
                            dp[i - 1][j - 1],     // case 2 替换
                            Math.min (
                                    dp[i - 1][j], // case 3 删除
                                    dp[i][j - 1]  // case 4 插入
                            )
                    ) + 1;
                }
            }
        }
        return dp[m][n];
    }

}
