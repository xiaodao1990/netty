package com.jenkin.netty.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO模型实例：
 *      1、使用BIO模型编写一个服务器端，监听6666端口，当有客户端连接时，就启动一个线程与之通信。
 *      2、要求使用线程池机制改善，可以连接多个客户端。
 *      3、服务器端可以接收客户端发送的数据(客户端用telnet模拟即可)
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        // 1、创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 2、创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("线程ID: " + Thread.currentThread().getId() + ", 线程Name: " + Thread.currentThread().getName() + "服务器启动了");
        while (true) {

            System.out.println("线程ID: " + Thread.currentThread().getId() + ", 线程Name: " + Thread.currentThread().getName() + "等待连接");
            // 3、服务端监听客户端的连接(服务端要一直监听使用while(true))
            final Socket accept = serverSocket.accept();
            System.out.println("线程ID: " + Thread.currentThread().getId() + ", 线程Name: " + Thread.currentThread().getName() + "连接到一个客户端"+accept.getPort());

            // 4、获取客户端连接后，处理客户端发送来的数据
            executorService.execute(new Runnable() {
                public void run() {
                    handler(accept);
                }
            });
        }
    }

    private static void handler(Socket accept) {
        // 1、读取客户端发送过来的数据
        try {
            InputStream inputStream = accept.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println("客户端Port: " + accept.getPort() + ", 线程Name: " + Thread.currentThread().getName() + ", len="+len + "-----------" +new String(bytes, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("客户端Port: " + accept.getPort() + " 关闭client连接");
            if (accept != null) {
                try {
                    accept.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
