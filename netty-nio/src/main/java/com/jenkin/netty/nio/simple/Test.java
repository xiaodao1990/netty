package com.jenkin.netty.nio.simple;

import io.netty.util.NettyRuntime;

public class Test {

    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors() * 2);
    }
}
