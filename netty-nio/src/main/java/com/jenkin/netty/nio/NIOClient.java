package com.jenkin.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws Exception {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 提供连接服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作");
            }
        }
        // 如果连接成功，就发送数据
        String str = "hello, Netty";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
