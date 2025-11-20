import java.util.*;

import static java.lang.System.in;

public class SlidingWindow {

    public static void main(String args[]) {
        SlidingWindow test = new SlidingWindow();

//        String s = "dvdf";
//        int result = test.lengthOfLongestSubstring(s);


        String s = "cbaebabacd";
        String p = "abc";

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
        // 需要一个set检查字符
        // 以窗口长度是否为p的长度作为判定是否全在的标准
        // 出现第一个全在的，输出l ，然后l++, 再看下一个是不是也是
        // 出现第一个单个字母在的，r++检查，直到length == set.size
        // 出现第一个不在的l = 不在+1，r = l
        List<Integer> result = new ArrayList<>();
        if (p.length() == 0) {
            return result;
        }

        int l = 0;
        int r = 0;
        Set<Character> pSets = new HashSet<>();
        char[] sChars = s.toCharArray();
        for (char target : p.toCharArray()) {
            pSets.add(target);
        }
        while(r < s.length()) {
            // 1、窗口固定为p的长度检查
            // 2、符合标准，窗口右移
            while (!pSets.isEmpty()) {
                if (r == s.length()) {
                    // 退出条件：最后一个字符也检查过了, 不满足条件
                    return result;
                }
                // 没符合标准就挨着找
                if (pSets.contains(sChars[r])) {
                    // 去重（每使用一个就删除一个）
                    pSets.remove(sChars[r]);
                    // 包含一个，右指针+1
                    r++;
                } else {
                    // 当前不包含
                    l = ++r;
                    // 更新去重集
                    for (char target : p.toCharArray()) {
                        pSets.add(target);
                    }
                }
            }
            // 检查完符合标准长度了，添加到结果集
            result.add(l);
            // 补充最新的去重集
            for (int i = l + 1; i < r; i++) {
                pSets.add(sChars[i]);
            }
            // 进位
            l++;
        }
        return result;
    }
}
