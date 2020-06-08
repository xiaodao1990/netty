package com.jenkin.netty.nio;

import java.nio.IntBuffer;

/**
 * Buffer的基本使用
 */
public class BasicBuffer {

    public static void main(String[] args) {
        // 1、创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 2、向buffer中存入数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 3、如何从buffer读取数据，切记:buffer需要读写切换
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
