package com.jenkin.netty.nio.nettygroupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * 添加私聊需求：
 *      1) 给每个用户定义，账号密码系统
 *      2) 将用户的channel通道根据Mao(User, Channel)保存
 *      3) 当用户进行私聊时，通过User信息找到Channel，Channel.writeAndFlush()完成发送
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 连接建立，第一个被执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其它的在线客户端
        // 该方法会将channelGroup中所有的channel遍历，并发送消息，不需要自己遍历
        channelGroup.writeAndFlush("【客户端】"+channel.remoteAddress()+" 加入聊天室\n");
        channelGroup.add(channel);
    }

    /**
     * 表示channel处于活动状态，提示xxx上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了~~");
    }

    /**
     * 表示channel处于不活动状态，提示xxx离线了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了~~");
    }

    /**
     * 断开连接，将xxx客户离开的信息推送给其它客户端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【客户端】"+channel.remoteAddress()+" 离开聊天室\n");
        System.out.println("channelGroup size: "+channelGroup.size());
    }

    /**
     * 定义一个Channel组，管理所有的channel
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 遍历ChannelGroup，根据不同的情况，回送不同的消息
        for (Channel ch : channelGroup) {
            if (ch != channel) {// 如果不是当前channel，转发消息
                ch.writeAndFlush("【客户端】"+channel.remoteAddress()+" 发送了消息 "+msg + "\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }
}
