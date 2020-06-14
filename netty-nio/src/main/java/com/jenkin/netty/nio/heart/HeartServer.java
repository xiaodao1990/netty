package com.jenkin.netty.nio.heart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Netty心跳检测机制案例
 *  实例要求：
 *      1) 编写一个Netty心跳检测机制案例，当服务器超过3秒没有读时，就提示读空闲
 *      2) 当服务器超过5秒没有写操作时，就提示写空闲
 *      3) 实现当服务器超过7秒没有读或写操作时，就提示读写空闲
 */
public class HeartServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))// 给BossGroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * IdleStateHandler是netty提供的处理空闲状态的处理器
                             * IdleStateHandler(int readerIdleTimeSeconds,int writerIdleTimeSeconds,int allIdleTimeSeconds)
                             * 说明：
                             * readerIdleTimeSeconds：表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                             * writerIdleTimeSeconds: 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                             * allIdleTimeSeconds：表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                             *
                             * IdleStateHandler触发后，就会传递给管道的下一个handler去处理，通过调用下一个
                             * handler的userEventTiggered
                              */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的自定义Handler
                            pipeline.addLast(new HeartServerHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.bind(6889).sync();
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
