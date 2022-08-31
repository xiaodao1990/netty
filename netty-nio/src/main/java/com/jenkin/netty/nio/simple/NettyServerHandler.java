package com.jenkin.netty.nio.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 接收客户端的数据和发送消息给客户端，交给channel关联的pipeline里面的handler来处理。
 *
 * 我们自定义一个Handler，需要继承netty规定好的HandlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件(读取客户端发送过来的消息)
     * @param ctx 上下文对象，含有管道pipeline，channel
     * @param msg 客户端发送过来的数据
     * @throws Exception
     */
    /*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("服务器读取线程： "+Thread.currentThread().getName());
        System.out.println("Server ctx: "+ctx);
        System.out.println("看看channel和pipeline的关系");
        Channel channel = ctx.channel();
        // 本质是一个双向链表，出栈入栈
        ChannelPipeline pipeline = ctx.pipeline();
        // 将msg转成ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送来的消息是："+buf.toString(CharsetUtil.UTF_8) + ", 客户端的地址是： "+ctx.channel().remoteAddress());
        super.channelRead(ctx, msg);
    }
    */

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        // 比如这里我们有一个非常耗时的业务逻辑-->异步执行-->提交到该channel对应的NioEventLoop的taskQueue中
//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端喵喵2\r\n", CharsetUtil.UTF_8));

        // 解决方案1 用户程序自定义的普通任务 该任务提交到taskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端喵喵2\r\n", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端喵喵3\r\n", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 解决方案2: 用户自定义定时任务-->该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端喵喵4\r\n", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);
        System.out.println("-----------go on--------------");
        super.channelRead(ctx, msg);
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
