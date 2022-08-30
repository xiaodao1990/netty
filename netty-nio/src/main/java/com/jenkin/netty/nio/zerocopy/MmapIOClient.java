package com.jenkin.netty.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by someone on 2022/8/30 10:32.
 */
public class MmapIOClient {
    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Fields
    // ===========================================================


    // ===========================================================
    // Constructors
    // ===========================================================


    // ===========================================================
    // Getter &amp; Setter
    // ===========================================================


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        socketChannel.configureBlocking(true);

        String           fileName         = "D:\\DevCache\\CodeDev\\netty\\netty-nio\\XunLeiWebSetup11.3.13.1940xl11.exe";
        long             startTime        = System.currentTimeMillis();
        FileChannel      channel          = new FileInputStream(fileName).getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        socketChannel.write(mappedByteBuffer);
        System.out.println("发送总字节数： " + channel.size() + ", 耗时： " + (System.currentTimeMillis() - startTime));

        socketChannel.close();
    }
}
