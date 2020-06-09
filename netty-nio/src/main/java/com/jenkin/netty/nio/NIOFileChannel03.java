package com.jenkin.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用FileChannel和read、write方法完成文件的拷贝
 * Channel--read-->Buffer--write-->Channel
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\file01.txt");
        FileChannel inChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\file02.txt");
        FileChannel outChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        inChannel.read(byteBuffer);
        byteBuffer.flip();
        outChannel.write(byteBuffer);
        fileInputStream.close();
        fileOutputStream.close();
    }
}
