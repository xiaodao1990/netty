package com.jenkin.netty.nio.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据，并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("服务器接收到信息如下：");
        System.out.println("长度="+len);
        System.out.println("内容="+new String(content, CharsetUtil.UTF_8));
        System.out.println("服务器接收到消息包数量="+(++count));
        System.out.println();

        // 回复消息
        String responseContent = UUID.randomUUID().toString();
        byte[] responseContent2 = responseContent.getBytes(CharsetUtil.UTF_8);
        int length = responseContent2.length;

        // 构建一个协议包，回复客户端
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(responseContent2);
        ctx.writeAndFlush(messageProtocol);
    }
}
