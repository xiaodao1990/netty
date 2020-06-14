package com.jenkin.netty.nio.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 实例需求：
 *  Http协议是无状态的，浏览器和服务器间的请求响应一次，下一次会重新创建连接
 *  需求：
 *      实现基于websocket的长连接的全双工的交互
 *      改变Http协议多次请求的约束，实现长连接，服务器可以发送消息给浏览器
 *      客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知;
 *      同样浏览器关闭了，服务器也会感知。
 */
public class WebSocketServer {

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
                            // 因为基于Http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块的方式写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
                             * 这就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * 1、对应websocket的数据是以帧(frame)的形式传递
                             * 2、可以看到WebSocketFrame下面有6个子类
                             * 3、浏览器请求时ws://localhost:6889/hello表示请求uri
                             * 4、WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，保持长连接
                             * 5、是通过一个状态码101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义handler，处理业务逻辑
                            pipeline.addLast(new WebSocketFrameHandler());

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
