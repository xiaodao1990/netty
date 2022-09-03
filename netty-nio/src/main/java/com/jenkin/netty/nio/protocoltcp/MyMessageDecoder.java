package com.jenkin.netty.nio.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<MessageProtocol> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode被调用");
        // 需要将得到的二进制字节码-->MessageProtocol数据包(对象)
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);

        // 封装成MessageProtocol对象，放入out，传递给下一个Handler业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
