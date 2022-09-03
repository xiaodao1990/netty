package com.jenkin.netty.nio.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * decode会根据接收的数据，被调用多次，知道确定没有新的元素被添加到list，或者Bytebuf
     * 没有更多的可读字节为止。
     * @param ctx 上下文对象
     * @param in  入站的ByteBuf
     * @param out List集合，将解码后的数据传给下一个handler处理
     * @throws Exception
     * 如果list不为空，则会将list的内容传递给下一个channelinboundHandler，该处理器的方法也会被调用多次
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode ByteToLongDecoder被调用");
        // 因为Long是8个字节,需要判断由8个字节，才能读取一个Long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
