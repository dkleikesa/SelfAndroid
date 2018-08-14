package com.ljz.javarun;

import java.util.Stack;

/**
 * 队列问题：
 * 使用两个栈来实现一个队列。
 * 解析：
 * 基本概念确认
 * 1. 栈：先进后出，最先入栈的被压到栈低，出栈的时候只能从栈定。
 * 2. 队列：先进先出，是一个排队的概念，从队尾入队列，从队头出队列。比较特殊的是循环队列，满了以后添加新元素会自动出队列
 * 3. 链表：一串非连续空间，通过指针互相连接起来。分为单向链表和双向链表
 *
 * 用两个栈实现一个队列，就是用两个先进后出，来实现一个先进先出。
 * 1. 队列push操作：用stack1来保存入队列的数据，
 * 2. 队列pop操作：stack1直接pop的话，返回的数据是反向的，因此，必须把stack1的数据pop出来，再添加到stack2中，
 *    再用stack2直接pop就可以得到正确顺序的数据
 * 3. pop操作的时候：只有每次当stack2为空的时候，才能再次去将stack1中的数据移动到stack2中
 */
public class QueueByStack {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            throw new RuntimeException("null");
        }
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }
}
