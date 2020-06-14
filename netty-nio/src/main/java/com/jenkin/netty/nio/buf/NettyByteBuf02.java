package com.jenkin.netty.nio.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {

    public static void main(String[] args) {

        // 创建ButeBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);

        // 使用相关api
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            System.out.println(new String(content, CharsetUtil.UTF_8));
            System.out.println(byteBuf.arrayOffset());// 0
            System.out.println(byteBuf.readerIndex());// 0
            System.out.println(byteBuf.writerIndex());// 12
            System.out.println(byteBuf.capacity());// 36
            System.out.println(byteBuf.readableBytes());// 12

        }
    }
}
