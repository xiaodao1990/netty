package com.jenkin.netty.nio.performance;

import io.netty.util.concurrent.FastThreadLocal;

/**
 * Created by someone on 2022/9/9 16:20.
 */
public class FastThreadLocalDemo {

    final class FastThreadLocalTest extends FastThreadLocal<Object> {

        @Override
        protected Object initialValue() throws Exception {
            return new Object();
        }
    }

    private final FastThreadLocalTest fastThreadLocalTest;

    public FastThreadLocalDemo() {
        fastThreadLocalTest = new FastThreadLocalTest();
    }

    public static void main(String[] args) {
        FastThreadLocalDemo threadLocalDemo0 = new FastThreadLocalDemo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Object o = threadLocalDemo0.fastThreadLocalTest.get();
                String name = Thread.currentThread().getName();
                try {
                    for (int i = 0; i < 10; i++) {
                        Object value = new Object();
                        threadLocalDemo0.fastThreadLocalTest.set(value);
                        System.out.println(name + ": o " + o + ", tmpO "+value);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Object obj = threadLocalDemo0.fastThreadLocalTest.get();
                String name = Thread.currentThread().getName();
                System.out.println(name + ": o " + obj);
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println(obj == threadLocalDemo0.fastThreadLocalTest.get());
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
