package algorithm.sword;

import java.util.Stack;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class Stack2Queue {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.add(node);
    }

    public int pop() {
        while (true) {
            if (stack2.size() > 0) {
                return stack2.pop();
            } else {
                while (stack1.size() != 0) {
                    stack2.add(stack1.pop());
                }
            }
        }

    }

}
