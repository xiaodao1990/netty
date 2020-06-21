package com.jenkin.netty.nio.rpc.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String para;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive被调用");
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        System.out.println("channelRead被调用 ");
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call被调用");
        context.writeAndFlush(para);
        // 由于异步调用，进行wait，等待结果返回
        wait();
        System.out.println("获取到服务器端返回的结果，notify被调用，唤醒当前线程");
        return result;
    }

    public void setPara(String para) {
        System.out.println("setPara");
        this.para = para;
    }
}
