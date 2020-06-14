package com.jenkin.netty.nio.nettygroupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(6889).run();
    }

    public void  run() throws InterruptedException {
        // 创建BossGroup、WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 向Pipeline添加解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 向Pipeline添加编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
            System.out.println("---------Netty 服务器启动成功----------");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
