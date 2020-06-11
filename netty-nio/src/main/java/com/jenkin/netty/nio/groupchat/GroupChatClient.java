package com.jenkin.netty.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private SocketChannel socketChannel;
    private Selector selector;
    private String username;

    // 完成初始化工作
    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 得到username
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {
        info = username + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        // 得到相关通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 得到一个Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    } else {
                        System.out.println("没有可用通道");
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    groupChatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 返送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            groupChatClient.sendInfo(msg);
        }
    }
}
