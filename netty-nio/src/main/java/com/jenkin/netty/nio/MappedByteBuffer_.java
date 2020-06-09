package com.jenkin.netty.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可以让文件直接在内存(堆外北村)中进行修改，而如何同步到文件中由NIO来完成。
 */
public class MappedByteBuffer_ {

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("G:\\DevCache\\Cache\\netty-parent\\Note\\file\\1.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE使用的读写模式
         * 参数2：0 可以直接修改的起始位置
         * 参数3：5 是映射到内存的大小(不是索引位置)，即将1.txt的多少个字节映射到内存
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(4, (byte) '9');
        //mappedByteBuffer.put(5, (byte) 'A');

        randomAccessFile.close();
        System.out.println("修改成功-------");
    }
}
