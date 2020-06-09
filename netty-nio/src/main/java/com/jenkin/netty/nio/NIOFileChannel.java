package com.jenkin.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel主要对本地文件进行IO操作，常见的方法有
 * read(ButeBuffer dst):从通道读取数据并放到缓冲区
 * write(ByteBuffer src):把缓冲区的数据写到通道中
 * transferFrom(ReadableByteChannel src, long position, long count):从目标通道中复制数据到当前通道
 * transferTo(long position, long count, WriteableByteChannel target):把数据从当前通道复制给目标通道
 *
 * 需求：使用前面学过的ByteBuffer和FileChannel，将“hello，Netty”写入到file01.txt中
 */
public class NIOFileChannel {

    public static void main(String[] args) throws Exception {

        String str = "hello，Netty";

        // 1、创建一个输入流
        FileOutputStream fileOutputStream = new FileOutputStream("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\file01.txt");

        // 2、通过FileOutputStream获取对应的FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 3、创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 4、将str放入到byteBuffer
        byteBuffer.put(str.getBytes());

        // 5、对byteBuffer进行flip()
        byteBuffer.flip();

        // 6、将byteBuffer数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();

    }
}
