package com.jenkin.netty.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        socketChannel.configureBlocking(true);

        String fileName = "D:\\DevCache\\CodeDev\\netty\\netty-nio\\XunLeiWebSetup11.3.13.1940xl11.exe";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送总字节数：" + transferCount + "，耗时： " + (System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
