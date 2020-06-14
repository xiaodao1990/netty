package com.jenkin.netty.nio.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 我们自定义一个Handler，需要继承netty规定好的HandlerAdapter
 */
@SuppressWarnings("all")
public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        // 读取从客户端发送的StudentPOJO.Student
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据id="+student.getId() + ", 名字="+student.getName());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存中，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端喵喵1\r\n", CharsetUtil.UTF_8));
        super.channelReadComplete(ctx);
    }

    /**
     * 处理异常，一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
