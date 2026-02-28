import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoPointerUtils {

    // 283. 移动零 https://leetcode.cn/problems/move-zeroes/?envType=study-plan-v2&envId=top-100-liked
    public void moveZeroes(int[] nums) {
        // 两个(快慢)指针，替换指针(i)确定0子数组的起点下标，目标指针(j)用来确定第一个非零需要往前提的内容下标
        // key: 双指针只有四种情况，i 符合 / 不符合条件，j 符合/不符合条件， 2x2=4
        int i = 0;
        int j = 0;
        while (j < nums.length) {
            // 如果目标值是0，那么无论num【i】,都要让j+1，下一步进行替换即可
            if (nums[j] == 0) {
                j++;
                continue;
            }
            // 如果目标值非0
            if (nums[j] != 0) {
                if (i == j) {
                    // i追赶上j的时候，说明没有符合条件的，j继续检查下一个
                    j++;
                    continue;
                }
                if (nums[i] != 0) {
                    // 当前待替换内容非0，移动待替换指针下标， 目标指针不动，直到替换指针追赶上目标指针
                    i++;
                } else {
                    // 是0，则每次只要换第一个就可以了，不用依次向后移动
                    nums[i] = nums[j];
                    nums[j] = 0;
                    i++;
                    j++;
                }
            }
        }
    }

    // 盛最多水的容器 https://leetcode.cn/problems/container-with-most-water/?envType=study-plan-v2&envId=top-100-liked
    public int maxArea(int[] height) {
        // 高取决于两个中的相对短边
        // 宽取决于下标差
        // 面积 = (j - i) * (min(height(i), height(j)))
        // 求面积最大
        // // 法1：两个for循环遍历找到最大值，时间复杂度O（n * n），会超出时间限制
        // int resultMaxArea = 0;
        // for (int i = 0; i < height.length; i++) {
        //     for (int j = i + 1; j < height.length; j++) {
        //         int currentMaxArea = (j - i) * (height[i] > height[j] ? height[j] : height[i] );
        //         if (resultMaxArea < currentMaxArea) {
        //             resultMaxArea = currentMaxArea;
        //         }
        //     }
        // }
        // return resultMaxArea;

        // 法2：找尽量长最大 * 尽量宽最大
        // 思路🌟：尽可能删掉一些不可能的值
        // ---> 题解的思路更好更简洁：🌟🌟左右指针对应的数字较小的那个指针不可能再作为容器的边界了【先用数学思路想清楚】
        // ---> 随着遍历，宽只会越来越窄，所以一定是要动高度相对小的那一边（因为算面积的时候的高是小的决定的）
        // 【key：从左往右找最高的，从右往左找最高的,直至相会】
        if (height.length == 0 || height.length == 1) {
            return 0;
        }
        int leftMaxPointer = 0;  // 最大面积时的左指针
        int shadowLeftPointer = leftMaxPointer;  // 探索最大面积的指针   （真正的双指针中的左指针）
        int rightMaxPointer = height.length - 1;  // 最大面积的右指针
        int shadowRightPointer = rightMaxPointer; // 探索最大面积的右指针 （真正的双指针中的右指针）
        int resultMaxArea = getArea(shadowLeftPointer, shadowRightPointer, height);

        while (shadowLeftPointer != shadowRightPointer) {
            // 一定要有一个要动
            // 🌟保留一个相对大的，小的进一 找找还有没有比当前大的
            if (height[shadowLeftPointer] > height[shadowRightPointer]) {
                shadowRightPointer--;
                while (height[shadowRightPointer] < height[rightMaxPointer]) {
                    if (shadowRightPointer == shadowLeftPointer) {
                        // 说明rightMaxPointer的高度 就是最大高度
                        // 又是最远，那就一定是最大面积
                        return resultMaxArea;
                    }
                    // 宽度本身就比之前小，高度还小，那面积不可能比之前大
                    shadowRightPointer--;
                }
                // 右侧找到比当前 右极 大的值，比一下大小
                int currentMaxArea = getArea(shadowLeftPointer, shadowRightPointer, height);
                if (currentMaxArea > resultMaxArea) {
                    rightMaxPointer = shadowRightPointer;
                    leftMaxPointer = shadowLeftPointer;
                    resultMaxArea = currentMaxArea;
                }
            } else {
                shadowLeftPointer++;
                while (height[shadowLeftPointer] < height[leftMaxPointer]) {
                    if (shadowLeftPointer == shadowRightPointer) {
                        return resultMaxArea;
                    }
                    shadowLeftPointer++;
                }
                int currentMaxArea = getArea(shadowLeftPointer, shadowRightPointer, height);
                if (currentMaxArea > resultMaxArea) {
                    rightMaxPointer = shadowRightPointer;
                    leftMaxPointer = shadowLeftPointer;
                    resultMaxArea = currentMaxArea;
                }
            }
        }

        return resultMaxArea;
    }

    private int getArea(int left, int right, int[] height) {
        int currentMaxArea = (right - left) * (height[right] > height[left] ? height[left] : height[right]);
        return currentMaxArea;
    }



    // 三数之和 https://leetcode.cn/problems/3sum/?envType=study-plan-v2&envId=top-100-liked
    // 多数之和，递归 第四个数 + 三数之和（双指针） = 目标值
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);  // 🌟先排序（只有排序才能找出数字特征）
        List<List<Integer>> result = new ArrayList<>();

        // 🌟 key1 ： 固定第一个数
        for (int i = 0; i < nums.length - 2; i++) {
            // 【去重1】跳过重复的第一个数
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // 优化：如果最小值都大于目标值，后面不可能有解
            if (nums[i] > 0) {
                break;
            }

            // 🌟 key 2 : 双指针找另外两个数
            int left = i + 1;
            int right = nums.length - 1;

            // 以i为基准 完成一整次遍历
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    // 找到一组解
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    // 【去重2】跳过重复的left
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    // 【去重3】跳过重复的right
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }

                    // 同时移动两个指针
                    left++;
                    right--;

                } else if (sum < 0) {
                    left++;   // 和太小，左指针右移   // 🌟key 3 确定动哪个指针
                } else {
                    right--;  // 和太大，右指针左移
                }
            }
        }

        return result;
    }


    // 接雨水 https://leetcode.cn/problems/trapping-rain-water/?envType=study-plan-v2&envId=top-100-liked
    public int trap(int[] height) {
        // 每列能获取到的雨水取决于 min（左，右最大高度） - 自身高度

//        // 1、动态规划——O（n）时间，O（n）空间 ✅
//        // key: 分别算出每个格子的左右最大高度，而左右最大高度数组的计算和前序的内容是相关的
//        int[] leftMaxArr = new int[height.length];
//        leftMaxArr[0] = 0; // 左侧起点特殊值（第一个的左边没有墙，即高度为0）
//        int[] rigtMaxArr = new int[height.length];
//        rigtMaxArr[height.length - 1] = 0;
//        for (int i = 1; i < height.length; i++) {
//            if (leftMaxArr[i - 1] < height[i - 1]) {
//                // 说明出现新的较高值
//                leftMaxArr[i] = height[i - 1];
//            } else {
//                // 维持原有较高值
//                leftMaxArr[i] = leftMaxArr[i-1];
//            }
//        }
//        for (int j = height.length - 2; j >= 0; j--) {
//            if (rigtMaxArr[j + 1] < height[j + 1]) {
//                rigtMaxArr[j] = height[j + 1];
//            } else {
//                rigtMaxArr[j] = rigtMaxArr[j+1];
//            }
//        }
//
//        // 计算每个格子的雨水数
//        int result = 0;
//        for (int x = 0; x < height.length; x++) {
//            int minSideHeight = Math.min(leftMaxArr[x], rigtMaxArr[x]);
//            int currentHeight = height[x];
//            if (minSideHeight <= currentHeight) {
//                // 说明左右都没当前高，接不住水
//                continue;
//            }
//            result +=  minSideHeight - currentHeight;
//        }
//        return result;


        // 2、双指针法 - O（n）时间，O（1）空间
        // key：高度只受左右中较小的值的影响（所以只要找出【左、右的 ”相对“ 小值】即可，因为另一边即使更大也没用，还是较小的决定了接水高度）
        // 头尾指针，边遍历，边计算，【谁小动谁, 找更大的】,结束条件是两边相遇
        int leftMaxValue = 0;
        int leftMaxPointer = 0;
        int rightMaxValue = 0;
        int rightMaxPointer = height.length - 1;
        int result = 0;
        while (leftMaxPointer != rightMaxPointer) {
            leftMaxValue = Math.max(leftMaxValue, height[leftMaxPointer]);
            rightMaxValue = Math.max(rightMaxValue, height[rightMaxPointer]);
            if (leftMaxValue <= rightMaxValue) {
                // 左边 较大值相比于右边的较大值的 较小，取左边的内容来计算
                if (leftMaxValue > height[leftMaxPointer]) {
                    // 只有可减的时候才减
                    result += leftMaxValue - height[leftMaxPointer];
                }
                leftMaxPointer++;
            } else {
                // 右边 较大值相比于左边的较大值的 较小，取右边的内容来计算
                if (rightMaxValue > height[rightMaxPointer]) {
                    result += rightMaxValue - height[rightMaxPointer];
                }
                rightMaxPointer--;
            }
        }
        return result;
    }

}
