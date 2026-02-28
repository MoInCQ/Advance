import java.util.*;

public class StackTest {
    public static void main(String args[]) {
    }


    // 有效的括号 https://leetcode.cn/problems/valid-parentheses/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean isValid(String s) {
        // 左括号压栈，遇到右括号则出栈构建对照表比对是否匹配
        // 不匹配则不合法，直到栈空也没不匹配的才认为合法
        char[] chars = s.toCharArray();
        Map<Character, Character> map = new HashMap<>();  // key：左括号  value：右括号
        map.put('{', '}');
        map.put('(', ')');
        map.put('[', ']');
        Stack<Character> stack = new Stack<>();
        for (char targetChar : chars) {
            if (map.containsKey(targetChar)) {
                // 是左括号压栈🌟
                stack.push(targetChar);
            } else {
                // 说明是右括号
                if (stack.isEmpty()) {
                    // 右括号没有对应的(右括号比左括号多)则直接return false；
                    return false;
                }
                // 右括号出栈 【对应的左括号】🌟
                char value = map.get(stack.pop());
                if (value != targetChar) {
                    // 右括号不匹配直接return false
                    return false;
                }
            }
        }
        return stack.empty();  // 左括号比右括号多的情况
    }


    // 最小栈  https://leetcode.cn/problems/min-stack/?envType=study-plan-v2&envId=top-100-liked
    // 法1：最小辅助栈 同步进出栈  需要O（n）额外辅助空间
    // 每次压栈的时候和辅助栈中的栈顶元素同步比较 minStack.push(Math.min(minStack.peek(), x));
    // 出栈的时候一起出


    // 法2:
    class MinStack {

        // 思路：两个栈---->空间换时间

        //主栈
        private Deque<Integer> stack;
        //最小栈
        private Deque<Integer> minStack;    // 不能用一个int变量来记录  因为比如当前的最小值pop之后，找不到前序的min值

        /** initialize your data structure here. */
        public MinStack() {
            stack = new LinkedList<>();
            minStack = new LinkedList<>();
        }

        public void push(int x) {

            //压入主栈
            stack.push(x);
            if (minStack.isEmpty()) {
                minStack.push(x);
            } else {
                minStack.push(Math.min(minStack.peek(), x));
            }

        }

        public void pop() {
            stack.pop();
            minStack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }


    // 字符串解码 https://leetcode.cn/problems/decode-string/description/?envType=study-plan-v2&envId=top-100-liked
    public String decodeString(String s) {
        // 字符是否是数字 Character.isDigit();
        // 字符是否是字母 Character.isLetter();

        // 思路：使用双栈 解决 k[encoded_string] 结构问题 ，
        // 栈1: 数字栈，🌟存倍数
        // 栈2：前置字符串栈，🌟 存的是每个 [ 之后的字符

        if (s == null || s.length() == 0) {
            return "";
        }
        LinkedList<Integer> numStack = new LinkedList<>();
        LinkedList<StringBuffer> preStrStack = new LinkedList<>();

        // 处理变量
        StringBuffer result = new StringBuffer();                   // 当前处理的str
        int count = 0;                                              // 倍数变量，处理多位数

        for (char c : s.toCharArray()) {
            // 1、数字，计算倍数【key：考虑有多位数的情况】
            if (c >= '0' && c <= '9') {
                // 🌟 直接背吧 倍数的计算公式 （1） * 10代表进位 （2） c + "" 代表char转字符串 （3）Integer.parseInt() 字符串转int
                count = count * 10 + Integer.parseInt(c + "");
                continue;
            }
            // 2、左括号—— 🌟 入栈 + 恢复处理变量
            if (c == '[') {
                // 入栈（1）入栈倍数 （2）入栈preStr
                numStack.push(count);
                preStrStack.push(result);
                // 压栈后， 恢复处理变量
                count = 0;
                result = new StringBuffer();
                continue;
            }
            // 3、右括号——🌟 出栈数字 + 乘倍数 + 出栈preStr并拼接
            if (c == ']') {
                int curCount = numStack.pop();
                StringBuffer appendStr = new StringBuffer();   // 处理乘倍数的str，此时的result是 [ result ]，即 “当前右括号” 和 “最近一个左括号” 之间的内容
                for (int i = 0; i < curCount; i++) {
                    appendStr = appendStr.append(result);
                }

                // 还原前置str
                StringBuffer preStr = preStrStack.pop();     //  此时的preStr是  上述 “最近一个左括号” 前面和 再上一个左括号之间的字母内容
                result = preStr.append(appendStr);
                continue;
            }
            // 4、普通字母
            result.append(c);
        }

        return result.toString();
    }


    // 每日温度 https://leetcode.cn/problems/daily-temperatures/?envType=study-plan-v2&envId=top-100-liked
    public int[] dailyTemperatures(int[] temperatures) {
        // 思路：单调栈， 【栈底到栈顶🌟单调递减  （在栈内的全都是还没有定位到下一个更高温度的🌟）】
        // 比较的是：index对应的温度
        // 存入的是：index
        LinkedList<Integer> stack = new LinkedList<>();
        int[] result = new int[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            // 遇到更大的值，需要把栈里所有的值都比一下
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                // 说明找到了targetIndex后面第一个更大的温度，出栈设置值
                int targetIndex = stack.pop();
                int days = i - targetIndex;
                result[targetIndex] = days;
            }
            // 当前温度入栈
            stack.push(i);
        }
        return result;
    }

}
