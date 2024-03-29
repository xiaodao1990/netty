package com.jenkin.netty.nio.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class HandlerServerInitializier extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new LongToByteEncoder());
        pipeline.addLast(new HandlerServerHandler());
    }
}
