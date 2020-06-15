package com.jenkin.netty.nio.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class HandlerClientInitializier extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个出站的handler，对数据进行一个编码
        pipeline.addLast(new LongToByteEncoder());
        // 加入一个入站的Handler，对数据进行解码
        pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new HandlerClientHandler());
    }
}
