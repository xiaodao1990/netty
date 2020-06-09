package com.jenkin.netty.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer支持类型化的put和get，put放入的是什么数据类型，get就应该使用相应的数据类型
 * 来取出，否则可能有BufferUnderflowException
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {

        // 1、创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        // 2、类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('伤');
        buffer.putShort((short) 4);

        buffer.flip();
        System.out.println("---------------");

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}
