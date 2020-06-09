package com.jenkin.netty.nio;

import java.nio.ByteBuffer;

/**
 * 可以将一个普通Buffer转成只读Buffer
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }

        // 读取
        buffer.flip();

        // 得到一个只读的Buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte) 100);
    }
}
