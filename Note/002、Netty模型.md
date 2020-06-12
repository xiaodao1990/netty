### Netty模型--简单版
![avatar](./pic/011_netty.png)  
```text
工作原理示意图：
    Netty主要基于主从Reactors多线程(如上图)做了一定的改进，其中主从Reactor多线程模型有多个Reactor。
机制梳理：
    1) BossGroup线程维护Selector，只关注Accept
    2) 当接收到accept事件，获取到对应的SocketChannel，封装成NIOSocketChannel并注册到Worder线程(作用：事件循环)，并进行维护。
    3) 当Worder线程监听到Selector中注册的通道发生感兴趣的事件后，就进行处理(就由Handler处理)，注意Handler已经加入到通道。  
```