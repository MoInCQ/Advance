import java.util.*;

public class SlidingWindow {

    public static void main(String args[]) {
        SlidingWindow test = new SlidingWindow();

//        String s = "dvdf";
//        int result = test.lengthOfLongestSubstring(s);


        String s = "abab";
        String p = "ab";

        List<Integer> result = test.findAnagrams(s, p);

    }


    // 无重复字符的最长子串 https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/?envType=study-plan-v2&envId=top-100-liked
    public int lengthOfLongestSubstring(String s) {

//        // 法1: 逐个查找每个字符的最长子串，比较长度，暴力法（时间复杂度是n的平方）
//        char[] array = s.toCharArray();
//        int result = 0;
//        Set<Character> set = new HashSet<>();  // 仅做不重复检查用（O（1）时间查找）
//        for (int start = 0; start < array.length; start++) {
//            int end = start;
//            // 寻找每个字符开头的最长字串
//            while (end < array.length && !set.contains(array[end])) {
//                set.add(array[end]);
//                end++;
//            }
//            // 出现重复的，则记录长度，清空set
//            result = Math.max(set.size(), result) ;
//            set.clear();
//        }
//        return result;

        // 法2: 滑动窗口（利用之前查过的内容 O（n）时间，O（n）空间） ✅
        char[] array = s.toCharArray();
        int result = 0;
        HashMap<Character, Integer> map = new HashMap<>();  // 仅做不重复检查用（O（1）时间查找） key：字符，value：字符所在下标
        int l = 0;
        int r = 0;
        // 只要右指针没走到头，就不算结束
        while (r < array.length) {
            // 先找最大不重复子串
            while (!map.containsKey(array[r])) {
                map.put(array[r], r);
                r++;
                if (r == array.length) {
                    result = Math.max(result, map.size());
                    return result;
                }
            }
            // 出现重复
            // 先比较并记录结果
            result = Math.max(result, map.size());
            // 继续从出现重复位置的下一个往后剩余部分找
            int nextL = map.get(array[r]) + 1;
            while (l < nextL) {
                map.remove(array[l]);
                l++;
            }
        }
        return result;
    }



    // 找到字符串中所有字母异位词 https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> findAnagrams(String s, String p) {
        // 法1:定长解法
        // key1：定长滑动窗口
        // key2: 用ASCII码来确定数组下标
        List<Integer> result = new ArrayList<>();

        // 边界条件检查
        if (s == null || p == null || s.length() < p.length()) {
            return result;
        }

        // pCount用于存储目标字符串p的字符统计
        // sCount用于存储滑动窗口内的字符统计
        int[] pCount = new int[26];
        int[] sCount = new int[26];

        // 1. 统计 p 的字符
        for (char c : p.toCharArray()) {
            pCount[c - 'a']++;
        }

        int pLen = p.length();
        int sLen = s.length();

        // 2. 初始化 s 的第一个窗口
        for (int i = 0; i < pLen; i++) {
            sCount[s.charAt(i) - 'a']++;
        }

        // 3. 检查第一个窗口是否匹配
        if (Arrays.equals(pCount, sCount)) {
            result.add(0);
        }

        // 4. 滑动窗口：i 代表即将移除的左边界字符的索引
        // i + pLen 代表即将加入的右边界字符的索引
        for (int i = 0; i < sLen - pLen; i++) {
            // 移除左边的字符
            sCount[s.charAt(i) - 'a']--;
            // 加入右边的字符
            sCount[s.charAt(i + pLen) - 'a']++;

            // 比较两个数组是否相同 (i + 1 是当前窗口的起始位置)
            if (Arrays.equals(pCount, sCount)) {
                result.add(i + 1);
            }
        }

        return result;



        // 法2 key：🌟下面这个是个变长的解法，定长的解法更好理解
        // 一个hashMap key是p的字符，value是当前滑动窗口字符匹配上的位置
//        char[] pChars = p.toCharArray();
//        HashMap<Character, Integer> pMap = new HashMap<>();
//        for (char pChar : pChars) {
//            pMap.put(pChar, -1);
//        }
//        List<Integer> result = new ArrayList<>();
//
//        // 左右指针开始构建滑动窗口
//        int left = 0;
//        int right = 0;
//        char[] sChars = s.toCharArray();
//        while (right < sChars.length) {
//            // 构建窗口
//            if (right - left < p.length()) {
//                char currentRightValue = sChars[right];
//                // 匹配上了
//                if (pMap.containsKey(currentRightValue)) {
//                    int location = pMap.get(currentRightValue);
//                    if (location >= left && location <= right) {   // 🌟 去重
//                        // 在当前窗口范围里，说明出现过，当前窗口出现重复
//                        // 更新位置，左指针进1,右指针也要+1
//                        pMap.put(currentRightValue, right);
//                        left++;
//                        right++;
//                    } else {
//                        // 不在当前窗口范围里，说明之前没出现过，右指针+1
//                        pMap.put(currentRightValue, right);
//                        right++;
//                    }
//                } else {
//                    // 没匹配上，左窗口和右窗口重置到相同右窗口+1位置
//                    left = ++right;
//                }
//            }
//            // 挪动窗口（窗口长度 == 当前目标长度的时候）
//            // 说明匹配上了 ，左指针++
//            if (right - left == p.length()) {
//                result.add(left);
//                left++;
//            }
//        }
//        return result;

    }
}
