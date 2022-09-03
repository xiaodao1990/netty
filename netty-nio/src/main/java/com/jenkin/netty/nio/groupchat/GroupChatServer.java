package com.jenkin.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 1、先编写服务器端
 *      服务器端启动并监听6667
 *      服务器端接收客户端信息，并实现转发(处理上线和离线)
 * 2、编写客户端
 *      客户端连接服务器
 *      客户端发送消息
 *
 * 需求：编写一个NIO群聊系统，实现服务器端和客户端之间的数据简单通讯(非阻塞)
 *       实现多人聊天，服务器端，可以监测用户上线，离线，并实现消息转发功能。
 *       客户端：通过channel可以无 阻塞发送消息给其它所有用户，同时可以接收其它用户发送的消息(通过服务器转发得到)
 *       目的：进一步了解NIO非阻塞网络编程机制
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void listen() {
        System.out.println("当前服务器的监听线程为："+Thread.currentThread().getName());
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线了, SocketChannel: " + socketChannel + ", hashCode: "+socketChannel.hashCode());
                        }
                        if (key.isReadable()) {
                            readDate(key);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待新时间。。。");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDate(SelectionKey key) {
        // 1、先定义SocketChannel
        SocketChannel socketChannel = null;
        try {
            // 2、得到channel
            socketChannel = (SocketChannel) key.channel();
            // 3、创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = socketChannel.read(buffer);
            // 根据count的值做处理
            if (count > 0) {
                // 把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                // 输出该消息
                System.out.println("from 客户端： "+msg);
                sendInfoToOtherClient(msg, socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                key.cancel();
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("当前服务器的处理线程为："+Thread.currentThread().getName());
        System.out.println("服务器转发消息中。。。");
        // 便利所有注册到selector上的SocketChannel，并排除self
        for (SelectionKey selectionKey : selector.keys()) {
            Channel targetChannel =  selectionKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                // 遍历获取到其他通道
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg存储到buffer中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }


    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
