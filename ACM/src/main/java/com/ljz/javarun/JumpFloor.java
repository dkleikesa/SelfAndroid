package com.ljz.javarun;

/**
 * 跳台阶问题：
 * 一个青蛙，一次可以跳一个台阶，也可以一次跳两个台阶，那么他跳上n个台阶的高楼，一共有多少种跳法？
 * 解析：
 * 统计分析的方法：
 * 假设n个台阶的跳法为f(n)
 * 1. 第一次跳一个台阶那么剩下的次数为f(n-1)
 * 2. 第一次跳两个台阶那么剩下的次数为f(n-2)
 * 3. 那么总的次数为f(n) = f(n-1)+ f(n-2)
 * 4. 循环运算即可得到最终的结果
 * 归纳法：
 * n=1   f(n)=1
 * n=2   f(n)=2
 * n=3   f(n)=3
 * n=4   f(n)=5
 * n=5   f(n)=8
 * n=6   f(n)=13
 * 归纳总结：f(n) = f(n-1)+ f(n-2)
 * <p>
 * <p>
 * <p>
 * 进阶跳台阶问题：
 * 一个青蛙，一次可以跳1台阶，也可以跳2个台阶、3个台阶、4个台阶。。。。n个台阶，那么他跳上n个台阶的高楼，一共有多少种跳法？
 * 解析：
 * 此问题为跳台阶问题的进阶，解决思路也类似
 * 1. 第一次跳1个台阶那么剩下的次数为f(n-1)
 * 2. 第一次跳2个台阶那么剩下的次数为f(n-2)
 * 3. 第一次跳3个台阶那么剩下的次数为f(n-3)
 * 4. 第一次跳n个台阶那么剩下的次数为f(n-n)
 * 5. 那么总的次数为 f(n) = f(n-1)+f(n-2)+...+f(n-n)
 * 6. 进也就是 f(n) = f(0)+f(1)+f(2)+...+f(n-1) 此结论即可作为计算依据
 * 7. 进一步推导：
 * f(n-1) =  f(0)+f(1)+f(2)+...+f(n-2)
 * f(n) = f(0)+f(1)+f(2)+...+f(n-2) + f(n-1)
 * 替换可得
 * f(n) = f(n-1)+f(n-1) = 2*f(n-1) 也就是 2^(n-1)
 */
public class JumpFloor {
    public static void run() {

        System.out.println("jumps:" + jump(10));
    }

    public static int jump(int target) {
        if (target <= 2) {
            return target;
        }
        int f1 = 1, f2 = 2, f = 0;
        for (int i = 3; i <= target; i++) {
            f = f1 + f2;
            f1 = f2;
            f2 = f;
        }
        return f;
    }

    public static int jumpII(int target) {
        if (target == 0) {
            return 0;
        }
        return (int) Math.pow(2.0, (target - 1));
    }
}
