package com.jenkin.netty.nio.performance;

/**
 * Created by someone on 2022/9/13 11:29.
 * https://blog.csdn.net/txk396879586/article/details/123485335
 */
public class ThreadLocalDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal0 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal1 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal2 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal3 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal4 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal5 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal6 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal7 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal8 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal9 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal10 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal11 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal12 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal13 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal14 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal15 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal16 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal17 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal18 = new ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    threadLocal.set("线程" + finalI + "的数据");
                    threadLocal0.set("线程" + finalI + "的数据0");
                    threadLocal1.set("线程" + finalI + "的数据1");
                    threadLocal2.set("线程" + finalI + "的数据2");
                    threadLocal3.set("线程" + finalI + "的数据3");
                    threadLocal4.set("线程" + finalI + "的数据4");
                    threadLocal5.set("线程" + finalI + "的数据5");
                    threadLocal6.set("线程" + finalI + "的数据6");
                    threadLocal7.set("线程" + finalI + "的数据7");
                    threadLocal8.set("线程" + finalI + "的数据8");
                    threadLocal9.set("线程" + finalI + "的数据9");
                    threadLocal10.set("线程" + finalI + "的数据10");
                    threadLocal11.set("线程" + finalI + "的数据11");
                    threadLocal12.set("线程" + finalI + "的数据12");
                    threadLocal13.set("线程" + finalI + "的数据13");
                    threadLocal14.set("线程" + finalI + "的数据14");
                    threadLocal15.set("线程" + finalI + "的数据15");
                    threadLocal16.set("线程" + finalI + "的数据16");
                    threadLocal17.set("线程" + finalI + "的数据17");
                    threadLocal18.set("线程" + finalI + "的数据18");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "====" + threadLocal.get());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    threadLocal.remove();
                }
            }, "ThreadLocal" + i).start();
        }
    }

}
