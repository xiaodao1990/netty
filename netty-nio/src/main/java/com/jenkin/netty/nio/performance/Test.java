package com.jenkin.netty.nio.performance;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author 小刀
 * @date 2022/09/14 22:17
 */
public class Test {

    private final int threadLocalHashCode = nextHashCode();

    /**
     * The next hash code to be given out. Updated atomically. Starts at
     * zero.
     */
    private static AtomicInteger nextHashCode =
            new AtomicInteger();

    private static final int HASH_INCREMENT = 0x61c88647;

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int hashCode = nextHashCode() & 15;
            System.out.println(hashCode);
        }
    }

}
