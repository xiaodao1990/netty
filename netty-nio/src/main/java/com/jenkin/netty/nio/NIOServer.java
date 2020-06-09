package com.jenkin.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 1、当客户端连接时，会通过ServerSocketChannel得到SocketChannel
 * 2、将SocketChannel注册到Selector上，register(Selector sel, int ops, Object att)，
 * 一个Selector上可以注册多个SocketChannel
 * 3、注册后返回一个selectionKey，会和该Selector关联(集合)
 * 4、Selector进行监听select方法，返回有事件发生的通道个数
 * 5、进一步得到各个selectionKey(有事件发生)
 * 6、在通过selectionKey反向获取SocketChannel，方法channel()
 * 7、可以通过channel,完成业务处理
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel注册到selector，关心事件为OP_ACCEPT 此处为注册服务端Channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待1秒，无连接");
                continue;
            }

            // 如果返回的不为0,就获取到相关的SelectionKey集合
            // 通过selectionKeys可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 根据selectionKey对应的通道发生的事件做相应的处理
                if (selectionKey.isAcceptable()) {// 有新的OP_ACCEPT
                    // 将该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel："+socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer 此处为注册客户端Channel
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (selectionKey.isReadable()) {// 发生OP_READ
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from 客户端："+new String(byteBuffer.array()));
                }
                // 手动从集合中移除当前的selectionKey，防止重复操作
                iterator.remove();
            }
        }
    }
}
