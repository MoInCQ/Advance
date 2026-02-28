import java.util.*;

public class HeapTest {
    public static void main(String[] args) {

    }



    // 经典快排
    public class QuickSort {
        public void quickSort(int[] nums, int left, int right) {
            // 结束条件：只有一个元素或没有元素时，不需要排
            if (left >= right) return;

            // 1. 获取分区点（排好序后的基准位置）
            int pivotIndex = partition(nums, left, right);

            // 2. 递归排序左边
            quickSort(nums, left, pivotIndex - 1);
            // 3. 递归排序右边
            quickSort(nums, pivotIndex + 1, right);
        }

        // 核心分区函数：挖坑法 (最直观)
        private int partition(int[] nums, int left, int right) {
            // 选最左边的数为基准 pivot，相当于这里挖了个坑
            int pivot = nums[left];

            while (left < right) {
                // 先 从右向左找，找比 pivot 小的数，填到左边的坑里
                while (left < right && nums[right] >= pivot) {
                    right--;
                }
                nums[left] = nums[right];

                // 再 从左向右找，找比 pivot 大的数，填到右边的坑里
                while (left < right && nums[left] <= pivot) {
                    left++;
                }
                nums[right] = nums[left];
            }

            // 最后把 pivot 填回剩下的那个坑（此时 left == right）
            nums[left] = pivot;

            return left; // 返回基准现在的下标
        }
    }

    // 数组中的第K个最大元素 https://leetcode.cn/problems/kth-largest-element-in-an-array/?envType=study-plan-v2&envId=top-100-liked
    public int findKthLargest(int[] nums, int k) {
        // 思路：每次快排确定一个mid的位置
        // 如果 mid == k 则当前 num[mid] 就是目标值
        // 如果 mid < k 只需要快排右边
        // 如果 mid > k 只需要快排左边
        int numsLength = nums.length;
        if (numsLength == 0 || k > numsLength) {
            return -1;
        }
        // 🌟注意这里是 numsLength - k，而不是 （numsLength - 1） - k，因为从语义上就是 “第k大”
        // eg 12345 第2大的 数就是 4 index = 5 - 2 = 3
        return quickSelectK(nums, 0, numsLength - 1, numsLength - k);
    }
    // 分区：双指针 交换
    private int quickSelectK(int[] nums, int l, int r, int k) {
        int partitionResult = partitionK(nums, l, r);
        if (partitionResult == k) return nums[k];  // 🌟 找到目标的值，即第k个位置的数字已经是快排的最终结果了
        if (partitionResult < k) return quickSelectK(nums, partitionResult + 1, r, k);  // 找右边
        return quickSelectK(nums, l, partitionResult - 1, k); // 找左边
    }

    // 只负责一次分区排序
    private int partitionK(int[] nums, int l, int r) {
        // 1、把左边作为分区mid，挖出值来
        int midValue = nums[l];
        while (l < r) {
            // 1、先找右边第一个小于mid的，填左边的坑
            while (l < r && nums[r] >= midValue) {
                r--;
            }
            nums[l] = nums[r];

            // 2、再找左边第一个大于mid的 填右边的坑
            while (l < r && nums[l] <= midValue) {
                l++;
            }
            nums[r] = nums[l];
        }
        // 此时l = r = mid 快排后的最终位置, mid值 填到中间
        nums[l] = midValue;
        return l;
    }




    // 前 K 个高频元素 https://leetcode.cn/problems/top-k-frequent-elements/description/?envType=study-plan-v2&envId=top-100-liked
    public int[] topKFrequent(int[] nums, int k) {
        // 思想：维护一个 size = k 的优先级队列（实现堆排序），遍历所有值构建好后输出
        // 1、需要一个【🌟（存储结构）map进行 数字 - 出现次数】的 存储
        // 2、构建【🌟优先级队列（用于顺序比较）】
        // 3、遍历队列输出


        // step 1
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        // step 2
        // 2.1 声明堆 ，要单独写一下 Comparator 因为要告知优先级队列比什么
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>(new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                // 构建的是 【小顶堆】
//                在 Java 的 Comparator 接口中，compare(a, b) 的返回值逻辑是：
//                负数 (< 0)：a 排在 b 前面（a < b）。
//                零 (0)：相等。
//                正数 (> 0)：a 排在 b 后面（a > b）。
//                口诀：🌟
//                        > "前减后" 是升序 (Ascending) -> o1 - o2
//                        > "后减前" 是降序 (Descending) -> o2 - o1
//                你的代码写的是 o1.getValue() - o2.getValue()，这是“前减后”，所以是升序。
                return o1.getValue() - o2.getValue();
            }
        });
        // 2.2 构建堆
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (queue.size() == k) {                                       // 🌟优先级队列永远只维护（ <= k）个元素
                if (entry.getValue() > queue.peek().getValue()) {
                    // 🌟当前出现频率比小顶堆（队头）更大（因为优先级队列是从小到大排过序的），删除队头，入队当前元素
                    queue.poll();
                    queue.offer(entry);
                }
                // 不如队头大的话，说明当前队列就是目标值，直接丢弃不用入队了，继续遍历
            } else {
                //  小于的时候直接入队
                queue.offer(entry);
            }
        }

        // step 3
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = queue.poll().getKey();   //  因为这里会出队 （虽然此时 K = queue.size() ） 所以for 的条件 i < queue.size() 会被影响，要使用固定值k
        }

        return  result;
    }
}
