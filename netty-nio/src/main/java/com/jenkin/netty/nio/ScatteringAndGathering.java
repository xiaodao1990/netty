package com.jenkin.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Scattering:将数据写入到Buffer时，可以采用Buffer数组，依次写入[分散]
 * Gathering:从Buffer读取数据时，可以采用Buffer数组，依次读进Channel
 */
public class ScatteringAndGathering {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;// 假定从客户端接收8个字节
        while (true) {// 循环的读取
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;// 累计读取的字节数
                System.out.println("byteRead=" + byteRead);
                for (int i = 0; i < byteBuffers.length; i++) {
                    ByteBuffer byteBuffer = byteBuffers[i];
                    System.out.println("byteBuffer"+i +", position="+byteBuffer.position() + ", limit=" + byteBuffer.limit());
                }
            }

            for (int i = 0; i < byteBuffers.length; i++) {
                byteBuffers[i].flip();
            }

            // 将数据读出显示到客户端
            int byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            for (int i = 0; i < byteBuffers.length; i++) {
                byteBuffers[i].clear();
            }
            System.out.println("byteRead="+byteRead+", byteWrite="+byteWrite+", messageLength="+messageLength);
        }

    }

}
