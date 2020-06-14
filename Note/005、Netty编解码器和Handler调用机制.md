### 基本说明
```text
1) Netty组件设置：Netty的主要组件有Channel、EventLoop、ChannelHandler、ChannelPipe等
2) ChannelHandler充当了处理入站和出战数据的应用程序逻辑的容器。例如，实现ChannelInboundHandler接口
(或ChannelInboundHandlerAdapter)，你就可以接收入站事件和数据，这些数据会被业务逻辑处理。当要给客户端
发送响应时，也可以从ChannelInboundHandler冲刷数据。业务逻辑通常写在一个或者多个ChannelInboundHandler中。
ChannelOutboundHandler原理一样，只不过它是用来处理出站数据的。
3) ChannelPipeline提供了ChannelHandler链的容器。以客户端应用程序为例，如果事件的运动方向是从
客户端到服务端的，那么我们称这些事件为出站，即客户端发送给服务端的数据会通过pipeline中的一系列
ChannelOutboundHandler，并被这些Handler处理，反之则称为入站。
```
![avatar](./pic/032_netty.png)