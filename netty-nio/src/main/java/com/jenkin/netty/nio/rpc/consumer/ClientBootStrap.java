package com.jenkin.netty.nio.rpc.consumer;

import com.jenkin.netty.nio.rpc.api.HelloService;

public class ClientBootStrap {

    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
        NettyClient customer = new NettyClient();
        // 创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);

        while (true) {
            Thread.sleep(2* 1000);
            String result = service.hello("你好，dubbo~");
            System.out.println("调用的结果result="+result);
        }

    }
}
