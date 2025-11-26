import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class NormalArrays {
    public static void main(String args[]) {
        NormalArrays normalArrays = new NormalArrays();

        int[] param = {-2,1,-3,4,-1,2,1,-5,4};
        int result = normalArrays.maxSubArray(param);
    }

    // 最大子数组和 https://leetcode.cn/problems/maximum-subarray/description/?envType=study-plan-v2&envId=top-100-liked
    public int maxSubArray(int[] nums) {

        // 法1 动态规划
        // 动态规划转移方程为 f(i) = Max(f(i - 1) + nums[i], num[i])

        // 假设我们正在遍历数组，当前遍历到元素 num。我们需要做一个决策：
        //  1、加入之前的序列：如果前面的子数组和是正数（比如 preSum > 0），那么加上当前元素 num 后，总和肯定比单拿 num 大。所以我们选择“接上”前面的队伍。
        //  2、另起炉灶：如果前面的子数组和是负数（preSum < 0），加上它只会拖累当前的元素 num（让和变小）。所以即使 num 也是负数，抛弃前面的负债自己重新开始计数，结果也会更好。

        if (nums.length == 0) {
            return 0;
        }

        int preMaxResult = 0; int result = nums[0];
        for (int num : nums) {
            // 找到当前轮次最大的
            preMaxResult = Math.max(preMaxResult + num, num);
            // 计算全局最大方案
            result = Math.max(result, preMaxResult);
        }
        return result;




//        // 法2（解决不了负数）前缀和，设最大和子数组为[i, j]， 求的是 Max(pre[j] - pre[i-1])
//        // ==> key: 要找到最大的pre[j] 和最小的pre[i-1] result = 最大值
//
//        // 处理下特殊值
//        if (nums.length == 0) {
//            return 0;
//        }
//        if (nums.length == 1) {
//            return nums[0];
//        }
//
//        int pre = 0;
////        int[] preArr = new int[nums.length]; （都不用这个数组）
//        int maxPre = Integer.MIN_VALUE;
//        int minPre = 0;
//        int result = 0;
//        for (int i = 0; i < nums.length; i++) {
//            pre += nums[i];
//            maxPre = Math.max(pre, maxPre);
//            minPre = Math.min(pre, minPre);
//            result = maxPre - minPre;
//        }
//        return result;
    }



    // 轮转数组 https://leetcode.cn/problems/rotate-array/description/?envType=study-plan-v2&envId=top-100-liked
    public void rotate(int[] nums, int k) {
        // 法1: 对 ( i + k ) % nums.length 取余得到要放的位置，放到一个临时数组，最后覆盖原数组
        // 但是需要额外数组 o(n）的时间和空间
//        int[] tempNums = new int[nums.length];
//        for (int i = 0; i < nums.length; i++) {
//            int targetIndex = (i + k) % nums.length;
//            tempNums[targetIndex] = nums[i];
//        }
//        for (int j = 0; j < nums.length; j++) {
//            nums[j] = tempNums[j];
//        }


        // 法2: 二次倒转，负负得正 时间复杂度2n - > o(n)  , o(1)的空间
        // 1234567 ， 3  ---> 目标 567 1234
        // 先整体倒转   7654321
        // key: 🌟k需要对length取余
        k = k % nums.length;
        swapArray(nums, 0, nums.length - 1);
        // 再分别倒转 0-(k-1) , k - (nums.length-1)  567 1234
        swapArray(nums, 0, k - 1);
        swapArray(nums, k, nums.length - 1);
    }

    // 调换顺序
    private int[] swapArray(int[] nums, int front, int rear) {
        while (front < rear) {
            int temp = nums[front];
            nums[front] = nums[rear];
            nums[rear] = temp;
            front++;
            rear--;
        }
        return nums;
    }



    // 合并区间 https://leetcode.cn/problems/merge-intervals/?envType=study-plan-v2&envId=top-100-liked
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][2];
        }
        // 1、先按起点排序
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        // 2、再合并区间输出
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            if (result.isEmpty()) {
                // 没值新开一个
                result.add(intervals[i]);
                continue;
            }
            int[] currentInterval = result.getLast();
            int l = intervals[i][0];
            int r = intervals[i][1];
            // 有值则比较右侧即可（左侧是升序的） (左侧不是升序则右五种可能 ，是升序只剩三种)
            if (currentInterval[1] >= r) {
                // （1）新值在当前值内，不处理
                continue;
            } else if (currentInterval[1] < l) {
                // （2） 新值在当前区间右侧
                result.add(intervals[i]);
            } else {
                // 新值和当前值右相交
                currentInterval[1] = r;
            }

        }
        return result.toArray(new int[result.size()][]);
    }



    // 除自身以外数组的乘积 https://leetcode.cn/problems/product-of-array-except-self/description/?envType=study-plan-v2&envId=top-100-liked
    public int[] productExceptSelf(int[] nums) {

    }


}
