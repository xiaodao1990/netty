package com.jenkin.netty.nio.tcp1;

/**
 * 要求客户端发送5个Message对象，客户端每次发送一个Message对象
 * 服务器端每次接收一个Message，分5次进行解码，每读取一个Message，会回复一个Message对象给客户端
 * 协议包: 相当于自定义了协议信息
 */
public class MessageProtocol {

    private int len;// 关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
