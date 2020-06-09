package com.jenkin.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用FileChannel和方法transferFrom完成文件的拷贝
 * transferFrom(ReadableByteChannel src, long position, long count):从目标通道中复制数据到当前通道
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\Koala.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\Koala1.jpg");

        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();

        outChannel.transferFrom(inChannel, 0, inChannel.size());

        // 关闭相关通道和流
        inChannel.close();
        outChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
