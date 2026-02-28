import java.util.*;
public class MatrixTest {
    public static void main(String args[]) {

        MatrixTest instance = new MatrixTest();

        int[][] matrix = {{1,2,3}, {4,5,6}, {7,8,9}};
        instance.rotate(matrix);
    }

    // 矩阵置零 https://leetcode.cn/problems/set-matrix-zeroes/description/?envType=study-plan-v2&envId=top-100-liked
    public void setZeroes(int[][] matrix) {
        // 读一遍结果，记录为0的行号和列号，分别用两个set记录（因为要O（1）的时间判定并去重，需要O（m+n）的空间复杂度）
        // 然后再读一遍设置值即可（读两遍 所以是O（mn）的时间复杂度）
        Set<Integer> zeroRowSet = new HashSet<>();
        Set<Integer> zeroColumnSet = new HashSet<>();
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (matrix[row][column] == 0) {
                    zeroRowSet.add(row);
                    zeroColumnSet.add(column);
                }
            }
        }
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (zeroRowSet.contains(row) || zeroColumnSet.contains(column)) {
                    matrix[row][column] = 0;
                }
            }
        }
    }

    // 螺旋矩阵 https://leetcode.cn/problems/spiral-matrix/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> spiralOrder(int[][] matrix) {
        // 顺时针螺旋的顺序是
        // （1）列数++ （2）行数++ （3）列数--（4）行数--
        // 每次换方向的时候 变化的那个方向 总数 - 1
        // 一圈一圈扫
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }
        // 【🌟 定义矩阵四个方向边界的位置】
        int left = 0, top = 0, right = matrix[0].length - 1, bottom = matrix.length - 1;
        while (left <= right && top <= bottom) {
            // 向右
            for (int i = left; i <= right; i++) {
                result.add(matrix[top][i]);
            }
            top++;
            if (top > bottom) break; // 没有需要扫的行了 必须是 >  不能是 == ，因为这个是【预处理】

            // 向下
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            right--;
            if (right < left) break;

            // 向左
            for (int i = right; i >= left; i--) {
                result.add(matrix[bottom][i]);
            }
            bottom--;
            if (bottom < top) break;

            // 向上
            for (int i = bottom; i >= top; i--) {
                result.add(matrix[i][left]);
            }
            left++;
            if (left > right) break;
        }
        return result;
    }


    // 旋转图像 https://leetcode.cn/problems/rotate-image/description/?envType=study-plan-v2&envId=top-100-liked
    public void rotate(int[][] matrix) {

        // 转90度的坐标转换公式  (i,j) --> (j, n-1-i)  画个3*3的图找规律
        //- 原行 i → 新列位置 = n-1-i（因为要翻转）
        //- 原列 j → 新行位置 = j（直接对应）

        // 法1: 如果不是要原地反转，直接遍历原矩阵按公式计算并填充结果矩阵就行了

        // 背景：原地交换场景
        // （1）主对角线（左上到右下）反转：(i, j) --> (j, i)
        // （2）反对角线反转：(i, j) --> (n - 1 - j, n - 1 -i)
        // （3）水平反转：（i, j）--> (i, n - 1 - j)
        // （3）垂直反转：（i, j）--> (n - 1 - i, j)

        // 法2: 因为要原地反转，而原公式不能实现原地反转
        // 所以要按上面这种可以原地交换的公式组合处理下
        // 先按主对角线反转，再水平反转
        // (i, j) --> (j, i) --> (j, n - 1 - i)

        // 先按对角线反转
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i; j < matrix[0].length; j++) { // 这里要从i开始，因为只处理右上半区就行了
                swapMatrix(matrix, i, j, j, i);
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length / 2; j++) { // 这里要 / 2 因为只在左半边交换就够了
                swapMatrix(matrix, i, j, i, matrix[0].length - 1 - j);
            }
        }
    }

    private void swapMatrix(int[][] originMatrix, int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        int temp = originMatrix[sourceRow][sourceColumn];
        originMatrix[sourceRow][sourceColumn] = originMatrix[targetRow][targetColumn];
        originMatrix[targetRow][targetColumn] = temp;
    }




    // 搜索二维矩阵 II https://leetcode.cn/problems/search-a-2d-matrix-ii?envType=study-plan-v2&envId=top-100-liked
    public boolean searchMatrix(int[][] matrix, int target) {
        // 法1: 直接z字型查找， 从右上角(0,column -1)开始 （行的相对最大和列的相对最小，左下角也有这个特征）
        int testRow = 0, testColumn = matrix[0].length - 1;
        while (testRow <= matrix.length - 1 && testColumn >= 0) {
            if (target == matrix[testRow][testColumn]) {
                return true;
            }
            if (matrix[testRow][testColumn] < target) {
                // 已经是当前行的最大值了，说明当前这一行都不满足，直接放弃行
                testRow++;
            } else {
                // 已经是当前列的最小值了，说明这一列都不满足，直接放弃列
                testColumn--;
            }
        }
        return false;
        // 法2:按行（二分查找），其实忽略了列会递增的特点
    }
}
