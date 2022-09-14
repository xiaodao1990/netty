package com.jenkin.netty.nio.performance;

/**
 * Created by someone on 2022/9/13 11:29.
 */
public class ThreadLocalDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    threadLocal.set("线程" + finalI + "的数据");
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
