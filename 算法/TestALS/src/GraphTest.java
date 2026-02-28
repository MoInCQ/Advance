import java.util.*;

public class GraphTest {
    public static void main(String args[]) {

    }


    // 岛屿数量 https://leetcode.cn/problems/number-of-islands/?envType=study-plan-v2&envId=top-100-liked
    public int numIslands(char[][] grid) {
        // 遇到1时， dfs/bfs（🌟上下左右）遍历二维数组，并将遇到的1置空为0（沉岛思想：确保图的遍历不会无限递归），遇到0则返回
        // result = dfs 触发的次数
        int result = 0;
        if (grid == null || grid.length == 0) {
            return result;
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    // 找到一个岛
                    result++;
                    // 开始dfs/bfs递归沉岛
//                    dfsSink(grid, i, j);
                    bfsSink(grid, i, j);
                }
            }
        }
        return result;

        // key： 不能只 “往右 + 往下 ”
        // 反例 eg
        // 1 1 1
        // 0 0 1
        // 1 1 1
    }

    // dfs
//    private void dfsSink(char[][] grid, int row, int column) {
//        if (row < 0 || column < 0 || row >= grid.length || column >=grid[0].length
//                || grid[row][column] == '0') {
//            return;
//        }
//        // 符合条件且 == 1的case下
//        // （1）沉岛                              // 🌟 递归遍历前需要先置位，避免无限递归
//        grid[row][column] = 0;
//        // （2）四个方向dfs递归遍历
//        dfsSink(grid, row - 1, column);    // 上
//        dfsSink(grid, row + 1, column);    // 下
//        dfsSink(grid, row, column - 1); // 左
//        dfsSink(grid, row, column + 1);  // 右
//    }

    // bfs
    private void bfsSink(char[][] grid, int row, int column) {
        Queue<int[]> targetQueue = new LinkedList<>();
        grid[row][column] = 0;
        targetQueue.offer(new int[]{row, column});
        while (!targetQueue.isEmpty()) {
            int[] nextIndex = targetQueue.poll();
            int nextRow = nextIndex[0];
            int nextColumn = nextIndex[1];
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] direction : directions) {
                int newRow = nextRow + direction[0];
                int newColumn = nextColumn + direction[1];
                if (newRow < 0 || newColumn < 0
                        || newRow >= grid.length || newColumn >=grid[0].length
                        || grid[newRow][newColumn] == '0') {
                    continue;
                }
                // 符合条件且 == 1的case下
                // （1）沉岛
                grid[newRow][newColumn] = '0';
                // （2）入队
                int[] newIndex = {newRow, newColumn};
                targetQueue.offer(newIndex);
            }
        }
    }


    // 腐烂的橘子 https://leetcode.cn/problems/rotting-oranges/?envType=study-plan-v2&envId=top-100-liked
    public int orangesRotting(int[][] grid) {
        // 层序遍历腐烂的橘子数，如果发现是新鲜橘子（1），则变为（2）
        // 问题转化为求腐烂的层数
        int freshCount = 0;
        int minutes = 0;
        if (grid == null || grid.length == 0) {
            return minutes;
        }
        int row = grid.length;
        int column = grid[0].length;
        Queue<int[]> levelQueue = new LinkedList<>();
        // 先遍历一遍，（1）找出 “🌟所有” 新鲜的橘子数 （2）构建 “🌟第一层” 腐烂的橘子的层序遍历队列
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (grid[i][j] == 2) {
                    int[] index = {i, j};
                    levelQueue.offer(index);
                } else if (grid[i][j] == 1) {
                    freshCount++;
                }
            }
        }
        // 开始层序遍历
        while(!levelQueue.isEmpty() && freshCount != 0) {
            // 记录🌟每层（每分钟）的腐烂橘子数量
            int levelSize = levelQueue.size();
            // 每遍历一层，结果++
            minutes++;
            for (int i = 0; i < levelSize; i++) {                       // 🌟按层遍历
                int[] targetIndex = levelQueue.poll();
                int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                for (int[] direction : directions) {                    // 遍历上下左右四个方向
                    int searchRow = targetIndex[0] + direction[0];      // 探索目标的坐标
                    int searchColumn = targetIndex[1] + direction[1];
                    if (searchRow >= 0 && searchRow < row && searchColumn >=0 && searchColumn < column
                            && grid[searchRow][searchColumn] == 1) {
                        grid[searchRow][searchColumn] = 2;              // 新鲜橘子置位腐烂
                        freshCount--;                                   // 新鲜橘子总数--
                        int[] searchIndex = {searchRow, searchColumn};
                        levelQueue.offer(searchIndex);                  // 腐烂橘子入队，为下一次作准备
                    }
                }
            }
        }
        return freshCount == 0 ? minutes : - 1;                          // 根据题意，能全部腐烂才返回分钟数，否则返回-1
    }


    // 课程表 https://leetcode.cn/problems/course-schedule/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 题解：https://leetcode.cn/problems/course-schedule/solutions/250377/bao-mu-shi-ti-jie-shou-ba-shou-da-tong-tuo-bu-pai-/?envType=study-plan-v2&envId=top-100-liked
        // 有向无环图 --> bfs找入度为0的值🌟

        //拓扑排序问题（把一个 有向无环图 转成 线性的排序 就叫 拓扑排序）
        //（1）根据依赖关系，构建邻接表🌟、入度数组🌟。
        //（2）选取入度为 0 的数据，根据邻接表，减小依赖它的数据的入度。
        //（3）找出入度变为 0 的数据，重复第 2 步。
        //（4）直至所有数据的入度为 0，得到排序，如果还有数据的入度不为 0，说明图中存在环

        if (numCourses == 0 || prerequisites == null || prerequisites.length == 0) {
            return true;
        }

        // （1）构建【邻接表(key: pre value:依赖pre的课程)】& 【入度数组】
        int[] inDegreeArray = new int[numCourses];
        HashMap<Integer, List<Integer>> dependencyMap = new HashMap<>();
        for (int[] dependencies : prerequisites) {
            int cur = dependencies[0];
            int pre = dependencies[1];

            inDegreeArray[cur]++;                                   // 入度+1

            List<Integer> dependList = dependencyMap.getOrDefault(pre, new ArrayList<>());
            dependList.add(cur);
            dependencyMap.put(pre, dependList);     // 构建邻接表（注意是 pre 为key🌟，从而实现O（1）查找 ）

        }

        // （2）构建（用于BFS）初始队列，放入入度为0的课程
        Queue<Integer> bfsQueue = new LinkedList<>();
        for (int i = 0; i < inDegreeArray.length; i++) {
            if (inDegreeArray[i] == 0) {
                bfsQueue.offer(i);                                 // 下标即修的课程号
            }
        }

        // (3) 开始bfs 修课程
        int finishCount = 0;
        while (!bfsQueue.isEmpty()) {
            int cur = bfsQueue.poll();
            finishCount++;                                          // 标记遍历到
            // 查找邻接表，依赖cur课程 入度--
            if (dependencyMap.containsKey(cur)) {
                List<Integer> dependList = dependencyMap.get(cur);
                for (int dependIndex : dependList) {
                    inDegreeArray[dependIndex]--;

                    if (inDegreeArray[dependIndex] == 0) {               // 新的入度为0的值入队
                        bfsQueue.offer(dependIndex);
                    }
                }

            }
        }
        return finishCount == numCourses;                            // 是否全部修过
    }


    // 延伸：有向图计算有没有环
    // v是顶点数，adj是有向边
//    核心思想是【不断移除入度为 0 的节点】。
//    计算所有节点的入度。
//    将入度为 0 的节点加入队列。
//    当队列不为空时，弹出节点，将其邻居的入度减 1。如果邻居入度变为 0，则加入队列。
//    记录弹出的节点数量。如果弹出的节点数 < 总节点数，说明剩下的节点形成了环（它们的入度都不为 0）。
    // eg：      GraphCycleBFS graph = new GraphCycleBFS(4);
    //        graph.addEdge(0, 1);
    //        graph.addEdge(1, 2);
    //        graph.addEdge(2, 0);
    //        graph.addEdge(2, 3);
    public boolean hasCycle(int V, List<List<Integer>> adj) {
        int[] inDegree = new int[V];

        // 1. 计算所有节点的入度
        for (int i = 0; i < V; i++) {
            for (int neighbor : adj.get(i)) {
                inDegree[neighbor]++;
            }
        }

        // 2. 将所有入度为 0 的节点加入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        int count = 0; // 记录拓扑排序能够访问的节点数

        // 3. BFS
        while (!queue.isEmpty()) {
            int u = queue.poll();
            count++;

            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                }
            }
        }

        // 4. 如果访问的节点数不等于总节点数，说明有环
        return count != V;
    }


    // 前缀树构建 https://leetcode.cn/problems/implement-trie-prefix-tree/?envType=study-plan-v2&envId=top-100-liked
    class Trie {

        // 🌟 核心结构：前缀树节点
        private class TrieNode {
            TrieNode[] children;   // (1) 可能的case数组 （因为是 a-z，连续数组模拟hash表）
            boolean isEnd;        // （2）当前节点是否为树的叶子节点

            public TrieNode() {
                this.children = new TrieNode[26];
                this.isEnd = false;
            }
        }

        private TrieNode root;                              // 树的根节点
        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char targetChar = word.charAt(i);
                int targetIndex = targetChar - 'a';
                if (node.children[targetIndex] == null) {
                    node.children[targetIndex] = new TrieNode();
                }
                node = node.children[targetIndex];          // 更新node，前缀树根节点不存储内容
                // 存在则无需处理
            }
            node.isEnd = true;                              // 更新前缀树结尾
        }

        public boolean search(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int i = c - 'a';
                if (node.children[i] != null) {
                    node = node.children[i];
                } else {
                    return false;
                }
            }
            return node.isEnd;
        }

        public boolean startsWith(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                int i = c - 'a';
                if (node.children[i] != null) {
                    node = node.children[i];
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}
