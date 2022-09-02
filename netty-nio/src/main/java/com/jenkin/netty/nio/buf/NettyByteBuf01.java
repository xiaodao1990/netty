package com.jenkin.netty.nio.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {

    public static void main(String[] args) {

        /**
         * 创建一个ByteBuf
         * 说明：
         *      1、创建对象，该对象包含一个数据arr，是一个byte[10]
         *      2、在netty的buffer中，不需要flip进行反转,由于底层维护了readIndex和WriteIndex
         *      3. 通过readIndex、writeIndex、capacity，将buffer分成三个区域。
         *          0~readIndex             已经读取的区域
         *          readIndex~writeIndex    可读的区域
         *          writeIndex~capacity     可写的区域
         *
         */
        ByteBuf buf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        System.out.println("capacity: "+buf.capacity());

        for (int i = 0; i < buf.capacity(); i++) {
//            System.out.println(buf.getByte(i));
            System.out.println(buf.readByte());
        }
    }
}
