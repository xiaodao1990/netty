package com.jenkin.netty.nio.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道添加处理器
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个netty提供的HttpServerCodec codec=>[coder-decoder]
        // HttpServerCodec是netty提供的处理http的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 增加一个自定义的handler
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
    }
}
