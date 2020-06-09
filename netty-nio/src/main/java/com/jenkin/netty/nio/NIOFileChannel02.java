package com.jenkin.netty.nio;


import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将file01.txt中的数据读入到程序中，并显示在控制台屏幕
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {
        // 1、创建文件的输入流
        File file = new File("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();

        // 2、创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 3、将数据从Channel读入到缓冲区
        fileChannel.read(byteBuffer);

        // 4、将byteBuffer的字节数据转成String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
