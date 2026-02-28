import java.util.*;

public class HashUtils {

        // 1. 两数之和 https://leetcode.cn/problems/two-sum/?envType=study-plan-v2&envId=top-100-liked
        // 法1: 头尾指针双层循环
        // public int[] twoSum(int[] nums, int target) {
        //     int rear = nums.length - 1;
        //     while (rear > 0) {
        //         for (int front = 0; front < rear; front++) {
        //             if (nums[front] + nums[rear] == target) {
        //                 return new int[]{front, rear};
        //             }
        //         }
        //         rear--;
        //     }
        //     return new int[0];
        // }
        // 法2：hashmap匹配
        public int[] twoSum(int[] nums, int target) {
            // key是当前值， value是其下标 --> 因为一定是一前一后 所以一定可以一次找到
            HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
            for (int front = 0; front < nums.length; front++) {
                if (indexMap.containsKey(target - nums[front])) {
                    return new int[]{front, indexMap.get(target - nums[front])};
                }
                indexMap.put(nums[front], front);
            }
            return new int[0];
        }

        // 128. 最长连续序列 https://leetcode.cn/problems/longest-consecutive-sequence/submissions/677422221/?envType=study-plan-v2&envId=top-100-liked
        public int longestConsecutive(int[] nums) {

            // 处理空数组情况
            if (nums.length == 0) {
                return 0;
            }

            // ❌ 因为只能拼接前后两个 无法递归拼接
//            // 因为时间复杂度要O（n），所以不能排序
//            // 1、map 存一下比较信息，key为数字序列
//
//            Map<Integer, List<Integer>> infoMap = new HashMap<Integer, List<Integer>>();
//            for (int i = 0; i < nums.length; i++) {
//                // 如果不存在，则新建
//                if (!infoMap.containsKey(nums[i])) {
//                    List<Integer> list = new ArrayList<Integer>();
//                    list.add(nums[i]);
//                    infoMap.put(nums[i], list);
//                }
//                // 检测有没有其他值是当前值的+1，是的话当前值 value 拼接上其他值数组 eg：2 ｜ 2， 3     // 🌟 先拼自己的后面
//                if (infoMap.containsKey(nums[i] + 1)) {
//                    infoMap.get(nums[i]).addAll(infoMap.get(nums[i] + 1));
//                }
//                // 检测当前值-1是否存在，存在的话把自己的结果List添加-1的value中                      // 🌟 再把自己拼给别人
//                if (infoMap.containsKey(nums[i] - 1)) {
//                    infoMap.get(nums[i] - 1).addAll(infoMap.get(nums[i]));
//                }
//            }
//            // 拼接出
//            // 100 ｜ []
//            // 4 | []
//            // 200 | []
//            // 1 | [1,2,3,4]
//            // 3 | [3, 4]
//            // 2 | [2, 3, 4]
//
//            // 然后比较values的最长长度取出value
//            Map.Entry<Integer, List<Integer>> resultEntry = null;
//            for (Map.Entry<Integer, List<Integer>> entry : infoMap.entrySet()) {
//                if (resultEntry == null) {
//                    resultEntry = entry;
//                }
//                if (entry.getValue().size() > resultEntry.getValue().size()) {
//                    resultEntry = entry;
//                }
//            }
//
//            return resultEntry.getValue().size();



            // ✅  🌟 （因为不要求原数组连续）使用HashSet来获取O（1）时间的查找

            // 处理空数组情况
            if (nums.length == 0) {
                return 0;
            }
            Set<Integer> hashSet = new HashSet<>();
            for (int num : nums) {
                hashSet.add(num); // 把 nums 转成哈希集合
            }

            // 遍历hashSet查找最长值
            int resultAddCount = 0;
            for (int num : hashSet) {
                if (hashSet.contains(num - 1)) {
                    //  🌟 不是起点（有前序）的直接排除，因为前序的一定会包含后面的（避免大量重复计算）
                    continue;
                }
                int currentAddCount = 0;
                while (hashSet.contains(++num)) {
                    currentAddCount++;
                }
                resultAddCount = Math.max(resultAddCount, currentAddCount);
            }
            return resultAddCount + 1;  // 这里要加一下自己
        }

    // 49. 字母异位词分组  https://leetcode.cn/problems/group-anagrams/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<String>> groupAnagrams(String[] strs) {
            // key：利用hashmap 存储 【🌟排序后】的字符串 比较并匹配
        // 1、先获取输入的每个内容的字符信息（【按升序排列】） key为字符信息，value为值列表
        Map<String, List<String>> infoMap = new HashMap<String, List<String>>();
        for (String str : strs) {
            char[] array = str.toCharArray();
            Arrays.sort(array);
            String key = new String(array);
            List<String> list = infoMap.getOrDefault(key, new ArrayList<String>());
            list.add(str);
            infoMap.put(key, list);
        }
        // 2、然后输出所有values
        return new ArrayList<List<String>>(infoMap.values());
    }
}
