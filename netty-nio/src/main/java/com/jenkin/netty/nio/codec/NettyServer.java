package com.jenkin.netty.nio.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * 实例要求：
 * 1) Netty服务器在6668端口监听，客户端能发送消息给服务器"hello,服务器~"
 * 2) 服务器可以恢复消息给客户端"hello，客户端"
 */
@SuppressWarnings("all")
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
                    .option(ChannelOption.SO_BACKLOG, 128)// 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)// 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 创建一个通道初始化对象
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 指定对哪种对象进行解码
                            pipeline.addLast("decoder", new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });// 给我们的workerGroup的EventLoop对应的管道设置处理器。
            System.out.println("--------服务器 is ready-------");
            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器(并绑定端口)
            ChannelFuture cf = bootstrap.bind(6668).sync();
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }
}
