package com.jenkin.netty.nio.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NetUtil;

/**
 * 实例要求：
 * 1) Netty服务器在6668端口监听，客户端能发送消息给服务器"hello,服务器~"
 * 2) 服务器可以恢复消息给客户端"hello，客户端"
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 1、创建两个线程组BossGroup和WorkerGroup
        // BossGroup只是处理连接请求，真正和客户端业务处理，会交给WorkerGroup，两个都是无限循环
        // bossGroup和workerGroup含有子线程(NioEventLoop)的个数默认是CPU的核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 2、创建服务器的启动对象，配置参数
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup)// 设置两个线程组
                    .channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务器的通道实现
                    // 为什么需要设置线程等待队列 https://blog.csdn.net/weixin_44730681/article/details/113728895
                    .option(ChannelOption.SO_BACKLOG, 128)// 设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)// 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 创建一个通道初始化对象

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 给pipeline尾部增加处理器
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });// 给我们的workerGroup的EventLoop对应的管道设置处理器。
            System.out.println("--------服务器 is ready-------");
            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器(并绑定端口)
            ChannelFuture future = bootstrap.bind(6668);// 异步执行
            System.out.println("--------服务器 bind port incomplete------");
            ChannelFuture cf   = future.sync();// 等待异步执行的结果【将异步转同步】
            System.out.println("--------服务器 bind port success-------");
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
            System.out.println("--------服务器 close success-------");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
