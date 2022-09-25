package com.jenkin.netty.nio.performance;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

/**
 * Created by someone on 2022/9/9 16:20.
 */
public class FastThreadLocalDemo {


    private static FastThreadLocal<String> threadLocal0 = new FastThreadLocal<>();
    private static FastThreadLocal<String> threadLocal1 = new FastThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            int finalI = i;
            new FastThreadLocalThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(threadLocal0.get());
                    threadLocal0.set("线程" + finalI + "的数据0");
                    threadLocal1.set("线程" + finalI + "的数据1");
                    System.out.println("threadLocal0 " + threadLocal0.get());
                    System.out.println("threadLocal1 " + threadLocal1.get());
                }
            }, "FastThreadLocalThread" + i).start();
        }
    }
}
