package com.jenkin.netty.nio.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HandlerClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new HandlerClientInitializier());

            ChannelFuture cf = bootstrap.connect("127.0.0.1", 7000).sync();
            cf.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
