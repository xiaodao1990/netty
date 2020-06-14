package com.jenkin.netty.nio.nettygroupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatClient("127.0.0.1", 6889).run();
    }

    public void run() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 向Pipeline添加解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 向Pipeline添加编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            Channel channel = cf.channel();
            System.out.println("-------------"+channel.localAddress()+" 客户端已经准备好了-----------");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\r\n");
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
