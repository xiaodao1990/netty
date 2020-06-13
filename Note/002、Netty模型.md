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

### Netty模型--进阶版
![avatar](./pic/012_netty.png)  
```text
工作原理示意图(如上图)
    Netty主要基于主从Reactors多线程模型做了一定改进，其中主从Reactor多线程模型中有多个Reactor。
```

### Netty模型--详细版
![avatar](./pic/013_netty.png)  
```text
工作原理示意图(见上图)
    1) Netty抽象出两组线程池，BossGroup专门负责接收客户端的连接，WorkerGroup专门负责网络的读写。
    2) BossGroup和WorkerGroup类型都是NIOEventLoopGroup
    3) NIOEventLoopGroup相当于一个事件循环组，这个组中含有多个事件循环，每个事件循环是NIOEventLoop。
    4) NIOEventLoop表示一个不断循环执行处理任务的线程。每个NIOEventLoop都有一个Selector对象，用于监听
    绑定在其上的socket的网络通讯。
    5) NIOEventLoopGroup可以有多个线程，即可以含有多个NIOEventLoop。
    6) 每个Boss NIOEventLoop执行步骤有3步
        6.1) 轮询accept事件
        6.2) 处理accept事件,与client建立连接，生成NIOSocketChannel，并将其注册到某个Worker NIOEventLoop
        的Selector上。
        6.3) 处理任务队列中的任务，即runAllTasks。
    7) 每个Worker NIOEventLoop循环执行的步骤。
        7.1) 轮询read，write事件。
        7.2) 处理I/O事件，即read、write事件，在对应的NIOSocketChannel中处理。
        7.3) 处理任务队列的任务，即runAllTasks。
    8) 每一个Worker NIOEventLoop在处理业务时，会使用pipeline(管道)，pipeline中包含channel，即通过pipeline可以
    获取到对应的通道，管道中维护了很多处理器。
```