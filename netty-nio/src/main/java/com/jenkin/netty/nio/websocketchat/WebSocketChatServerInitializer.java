package com.jenkin.netty.nio.websocketchat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocketChatServer ChannelInitializer.
 *
 * @since 1.0.0 2020年1月1日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
public class WebSocketChatServerInitializer extends
        ChannelInitializer<SocketChannel> {	//（1）

    @Override
    public void initChannel(SocketChannel ch) throws Exception {//（2）
        ChannelPipeline pipeline = ch.pipeline();

        /**
         * WebSocket 通过“Upgrade handshake（升级握手）”从标准的HTTP或HTTPS协议转为WebSocket。
         * 因此，使用WebSocket的应用程序将始终以HTTP/S开始，然后进行升级。
         */
        // 因为基于Http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        /**
         * 1、Http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合。
         * 2、这就是为什么，当浏览器发送大量数据时，就会发生多次http请求。
         */
        pipeline.addLast(new HttpObjectAggregator(8192));
        // 以块的方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpRequestHandler("/ws"));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler());

    }
}