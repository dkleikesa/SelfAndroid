package com.ljz.qcmian;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        test t1 = new test(1);
        test t2 = new test(2);
        System.out.println(t1.A);
        System.out.println(t2.A);
        assertEquals(4, 2 + 2);
    }

    private static class test {
        final int A;

        public test(int a) {
            A = a;
        }
    }
}