import java.util.HashMap;
import java.util.Map;

public class SubString {

    public static void main(String arg[]) {

    }


    // 和为k的子数组 https://leetcode.cn/problems/subarray-sum-equals-k/?envType=study-plan-v2&envId=top-100-liked
    public int subArraySum(int[] nums, int k) {
        // key1：🌟利用前缀和map pre  即 当前这个下标的值 + 前面所有数 的和

        // key2: 🌟抽象一下目标
        // 目标：获取到 [i, j] 的和为k
        // 基于前缀和 pre[j] - pre[i - 1] = k
        // 移位：pre[i-1] = pre[j] - k
        // ==》 找出符合条件的前缀和的值的个数
        // ==> key3: 🌟hash表存前缀和，key：前缀和，value：该前缀和出现的次数

        Map<Integer, Integer> preMap = new HashMap<>();

        int pre = 0;
        // 初始化第一个前缀和，前缀和0是出现过的 ---> 确保单个值就是目标值的情况
        preMap.put(pre, 1);

        int result = 0;

        // 边遍历边构建前缀和map
        for (int num : nums) {
            pre += num;
            // 判断pre - k有没有存在过，存在过说明之前有过一个下标（i-1）他的前缀的和的位置是满足条件的
            if (preMap.containsKey(pre - k)) {
                result += preMap.get(pre - k);
            }
            preMap.put(pre, preMap.getOrDefault(pre, 0) + 1);
        }
        return result;
    }

}
