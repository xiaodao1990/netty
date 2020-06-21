package com.jenkin.netty.nio.rpc.provider;

import com.jenkin.netty.nio.rpc.api.HelloService;

public class HelloServiceImpl implements HelloService {

    private static int count = 0;

    @Override
    public String hello(String msg) {

        System.out.println("收到客户端消息："+msg);
        // 根据msg返回不同结果
        if (msg != null) {
            return "你好客户端，我已经收到你的消息 " + msg +"第" + (++count) + "次";
        }
        return "你好客户端，我已经收到你的消息";
    }
}
