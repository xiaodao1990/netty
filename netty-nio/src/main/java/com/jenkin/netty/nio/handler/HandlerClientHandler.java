package com.jenkin.netty.nio.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class HandlerClientHandler extends SimpleChannelInboundHandler<Long> {

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HandlerClientHandler发送数据");
        ctx.writeAndFlush(123456L);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的IP="+ctx.channel().remoteAddress());
        System.out.println("收到服务器消息="+msg);
    }
}
