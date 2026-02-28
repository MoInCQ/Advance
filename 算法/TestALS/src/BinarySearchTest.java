import java.util.*;
public class BinarySearchTest {


    public static void main(String args[]) {
        BinarySearchTest instance = new BinarySearchTest();

//        int[][] input = {{1,3,5,7},{10,11,16,20},{23,30,34,60}};
//        instance.searchMatrix(input, 3);

//        int[] input = {1,3};
//        instance.searchInsert(input, 3);

//        int[] input = {5,7,7,8,8,10};
//        instance.searchRange(input, 8);

        int[] input = {4, 5, 6, 1, 2, 3};
        instance.findMin(input);
    }

    // 搜索二维矩阵 https://leetcode.cn/problems/search-a-2d-matrix/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean searchMatrix(int[][] matrix, int target) {
        // z字查找，从右上(可以判定是行变还是列变)开始找
        int row = 0; int column = matrix[0].length - 1;
        while (row < matrix.length && column >= 0) {
            if (matrix[row][column] == target) {
                return true;
            }
            if (matrix[row][column] < target) {
                row++;  // 这一行的最大值都比目标小，行+1
            } else {
                column--;  // 目标值大于target，因为行是递增的，又因为下一行开始大于当前行最大，所以只可能在当前这一行
            }
        }
        return false;
    }


    // 搜索插入位置
    public int searchInsert(int[] nums, int target) {
        // 二分查找确定坐标位置
        // 转换问题概念：在一个有序数组中找第一个大于等于 target 的下标 🌟
        // 法1：迭代
        if (nums.length == 0) return 0;
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] > target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        // start = end 时单独判定
        // （1）> 时表示没匹配上，且当前值比target大，那就把target放到当前位置，原当前位置的往后挤
        // （2）= 时表示匹配上了，当前start = end
        // （3）< 时表示没匹配上，且当前值比target还小，那target应该在当前位置的下一个
        return nums[start] >= target ? start : start + 1;



        // 法2: 递归处理
//        return binarySearchInsertIndex(nums, target, 0, nums.length - 1);
    }
    private int binarySearchInsertIndex(int[] nums, int target, int start, int end) {
        int targetIndex = ((end - start) / 2) + start;
        // 判断当前值
        int currentValue = nums[targetIndex];
        if (currentValue == target) {
            return targetIndex;
        }
        if (currentValue < target) {
            if (targetIndex + 1 > end) {
                // 超限，没找到目标，目标插入位置就是当前 targetIndex +1 （因为当前值比目标值小，要放到下一个位置）
                return targetIndex + 1;
            }
            // 比目标小的话找右边
            return binarySearchInsertIndex(nums, target, targetIndex + 1, end);

        }

        if (targetIndex - 1 < start) {
            // 超限，没找到目标，目标插入位置就是 targetIndex （因为当前值比目标大，直接放在当前位置，把当前值往后挤一位）
            return targetIndex;
        }
        // 比目标大的话找左边
        return binarySearchInsertIndex(nums, target, start, targetIndex - 1);
    }

    // 在排序数组中查找元素的第一个和最后一个位置 https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked
    public int[] searchRange(int[] nums, int target) {
        // 二分查找分别往左、右二分，全部遍历一遍，通过【🌟备选值】来记录最后一个满足条件的
        int start = binarySearchRangeIndex(nums, target, true);
        int end = binarySearchRangeIndex(nums, target, false);
        int[] result = {start, end};
        return result;
    }

    // leftFind，区分是找左边界还是右边界
    private int binarySearchRangeIndex(int[] nums, int target, boolean leftFind) {
        int start = 0;
        int end = nums.length - 1;
        int ans = -1; // 记录备选值
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                ans = mid;
                if (leftFind) {
                    // 继续往左找
                    end = mid - 1;
                } else {
                    // 继续往右找
                    start = mid + 1;
                }
            } else if (nums[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        // 二分全部遍历一遍，返回备选值
        return ans;
    }

    // 搜索旋转排序数组 https://leetcode.cn/problems/search-in-rotated-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked
    public int search(int[] nums, int target) {
        // 迭代  二分的时候，判断往左找还是往右找的时候多加一个判定，【🌟判断是往哪个方向找】（升序最重要的就是提供了找的方向）
        // 将数组一分为二，其中一定有一个是有序的，另一个可能是有序，也能是部分有序。
        // 此时有序部分用二分法查找。无序部分再一分为二，其中一个一定有序，另一个可能有序，可能无序。就这样循环.
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            // 判断往那边走
            // 必须【（1）先判断哪边有序（2）再判断target在不在有序区间】 ==> 🌟只有这样才能完全排除一边
            if (nums[start] <= nums[mid]) {
                // 如果左边有序
                if (target < nums[mid] && target >= nums[start]) {
                    // 在这个有序区间里，就正常二分
                    end = mid - 1;
                } else {
                    // 否则可以确定在另一半区间
                    start = mid + 1;
                }
            } else {
                // 如果右边有序
                if (target > nums[mid] && target <= nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }

        }
        return -1;
    }

    //  寻找旋转排序数组中的最小值 https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked
    public int findMin(int[] nums) {
        // 二分查找选定候选最小值，
        // 旋转数组 一定有一边是有序的（局部仍然是有序），找最小值   // 🌟最小值一定在无序的一边

        int start = 0;
        int end = nums.length - 1;

        int minValue = Integer.MAX_VALUE;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] < minValue) {      // 对mid值的使用
                // 构建待选值
                minValue = nums[mid];
            }
            // 判断哪边有序
            if (nums[mid] <= nums[end]) {
                // 即 右边有序，则最小值一定在左边 eg： 5 6 1 2 3 4
                end = mid - 1;
            } else {
                // 即 左边有序, 则最小值一定在右边 eg：3 4 5 6 1 2  或  4 5 6 1 2 3
                start = mid + 1;
            }
        }
        return minValue;
    }

}
