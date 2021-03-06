### Netty服务启动源码剖析 参考https://www.cnblogs.com/xiangnan6122/p/10202257.html[源码解析系列文章]
```text
// 此处1表示bossGroup事件组有一个线程
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    -->NioEventLoopGroup(int nThreads, Executor executor);// nThreads=1
        -->NioEventLoopGroup(int nThreads, Executor executor, final SelectorProvider selectorProvider);
            -->NioEventLoopGroup(int nThreads, Executor executor, final SelectorProvider selectorProvider,final SelectStrategyFactory selectStrategyFactory)
                -->MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args)
                -->super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args)// (nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads)=1
                    -->MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args)
                        -->children = new EventExecutor[nThreads];
                        -->children[i] = newChild(executor, args);// NioEventLoopGroup.newChild
                            -->new NioEventLoop(this, executor, (SelectorProvider) args[0], ((SelectStrategyFactory) args[1]).newSelectStrategy(), (RejectedExecutionHandler) args[2]);
// 此处使用默认线程=cpu核数*2，即可以充分利用多核的优势
EventLoopGroup workerGroup = new NioEventLoopGroup();
    -->NioEventLoopGroup(int nThreads, Executor executor);// nThreads=1
        -->NioEventLoopGroup(int nThreads, Executor executor, final SelectorProvider selectorProvider);
            -->NioEventLoopGroup(int nThreads, Executor executor, final SelectorProvider selectorProvider,final SelectStrategyFactory selectStrategyFactory)
                -->MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args)
                    -->DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
                    -->super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args)// (nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads)=1
                        -->MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args)
                            -->children = new EventExecutor[nThreads];// nThreads=8 我的电脑为4核
                            -->for (int i = 0; i < nThreads; i ++)-->children[i] = newChild(executor, args); // NioEventLoopGroup.newChild
                                -->new NioEventLoop(this, executor, (SelectorProvider) args[0], ((SelectStrategyFactory) args[1]).newSelectStrategy(), (RejectedExecutionHandler) args[2]);
// ServerBootstrap是一个引导类，用于启动服务器和引导整个程序的初始化。
ServerBootstrap b = new ServerBootstrap();
    -->ServerBootstrap() { }
ServerBootstrap.group(bossGroup, workerGroup)
    -->ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)
        -->AbstractBootstrap(AbstractBootstrap<B, C> bootstrap)
        -->this.group = group;
        -->this.childGroup = childGroup;
ServerBootstrap.channel(NioServerSocketChannel.class)
    -->AbstractBootstrap.channel(Class<? extends C> channelClass)// channelClass
        -->new ReflectiveChannelFactory<C>(channelClass)
            -->this.clazz = clazz;// this.clazz=NioServerSocketChannel.class
        -->AbstractBootstrap.channelFactory(io.netty.channel.ChannelFactory<? extends C> channelFactory)
            -->channelFactory((ChannelFactory<C>) channelFactory);
                -->this.channelFactory = channelFactory;// this.channelFactory=ReflectiveChannelFactory
// 添加一些服务器启动的TCP参数
ServerBootstrap.option(ChannelOption.SO_BACKLOG, 100)
    // 将参数放入一个Map集合中
    -->options.put(option, value);// private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
// 为BossGroup添加日志处理器Handler
ServerBootstrap.handler(new LoggingHandler(LogLevel.INFO))
    -->AbstractBootstrap.handler(ChannelHandler handler)
        -->this.handler = handler;
// 为WorderGroup添加一个SocketChannel的Handler
ServerBootstrap.childHandler(new ChannelInitializer<SocketChannel>())
    -->AbstractBootstrap.childHandler(ChannelHandler childHandler)
        -->this.childHandler = childHandler;
// 
ServerBootstrap.bind(PORT)
    -->ChannelFuture bind(int inetPort);// AbstractBootstrap
        -->new InetSocketAddress(inetPort)
            -->this(InetAddress.anyLocalAddress(), port);// port=8007
                -->Inet6AddressImpl.anyLocalAddress()
                    -->anyLocalAddress = (new Inet4AddressImpl()).anyLocalAddress();
                        -->Inet4AddressImpl.anyLocalAddress()
                            -->anyLocalAddress = new Inet4Address();
                            -->anyLocalAddress.holder().hostName = "0.0.0.0";// 0.0.0.0 代表本机的所有ip地址
                    -->new InetSocketAddressHolder(null, addr == null ? InetAddress.anyLocalAddress() : addr, checkPort(port));        
                        -->InetSocketAddressHolder(String hostname, InetAddress addr, int port)
                            -->this.hostname = hostname;
                            -->this.addr = addr;
                            -->this.port = port;
        -->bind(new InetSocketAddress(inetPort));
            -->ChannelFuture bind(SocketAddress localAddress);// AbstractBootstrap
                -->doBind(localAddress);
                    -->initAndRegister();
                        -->channel = channelFactory.newChannel();// channelFactory为ServerBootstrap.channel(NioServerSocketChannel.class)中初始化的ReflectiveChannelFactory
                            // 通过ServerBootstrap的通道工厂反射创建一个NioServerSocketChannel
                            -->clazz.getConstructor().newInstance();// 实例化NioServerSocketChannel
                                -->NioServerSocketChannel()
                                    -->newSocket(DEFAULT_SELECTOR_PROVIDER);
                                        -->provider.openServerSocketChannel();// provider=WindowsSelectorProvider
                                            -->SelectorProviderImpl.openServerSocketChannel()
                                                -->new ServerSocketChannelImpl(this);
                                                    -->ServerSocketChannel(SelectorProvider provider)
                                                    -->AbstractSelectableChannel(SelectorProvider provider)
                                                        -->this.provider = provider;
                                    -->this(newSocket(DEFAULT_SELECTOR_PROVIDER));
                                        -->NioServerSocketChannel(ServerSocketChannel channel)// channel=ServerSocketChannelImpl
                                            -->AbstractNioMessageChannel(Channel parent, SelectableChannel ch, int readInterestOp)
                                            -->AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp)
                                            -->AbstractChannel(Channel parent)
                                                -->id = newId();// id=ChannelId DefaultChannelId - Dio.netty.machineId: 00:28:f8:ff:fe:78:56:c7 (auto-detected)
                                                -->unsafe = newUnsafe();
                                                    -->new NioMessageUnsafe();// 主要作用为初始化调用链上的成员变量
                                                        -->AbstractUnsafe()
                                                        -->AbstractNioUnsafe()
                                                        -->NioMessageUnsafe()// 用于操作消息。
                                                -->pipeline = newChannelPipeline();
                                                    // 创建一个DefaultChannelPipeline管道，是一个双向链表结构，用于过滤所有进出的消息
                                                    -->new DefaultChannelPipeline(this);
                                                        protected DefaultChannelPipeline(Channel channel) {
                                                            this.channel = ObjectUtil.checkNotNull(channel, "channel");
                                                            succeededFuture = new SucceededChannelFuture(channel, null);
                                                            voidPromise =  new VoidChannelPromise(channel, true);
                                                    
                                                            tail = new TailContext(this);
                                                            head = new HeadContext(this);
                                                    
                                                            head.next = tail;
                                                            tail.prev = head;
                                                        }
                                        // 创建一个NioServerSocketChannelConfig对象，用于对外展示一些配置
                                        -->config = new NioServerSocketChannelConfig(this, javaChannel().socket());
                        -->init(channel);
                            -->ServerBootstrap.init(Channel channel)
                                -->ChannelPipeline p = channel.pipeline();
                                -->p.addLast(new ChannelInitializer<Channel>())
                                    -->addLast(null, handlers);// DefaultChannelPipeline
                                        -->addLast(EventExecutorGroup executor, ChannelHandler... handlers)// DefaultChannelPipeline
                                            -->addLast(executor, null, h);
                                                -->addLast(EventExecutorGroup group, String name, ChannelHandler handler)// DefaultChannelPipeline
                                                    // 创建一个AbstractChannelHandlerContext
                                                    -->newCtx = newContext(group, filterName(name, handler), handler);
                                                    -->addLast0(newCtx);// 将新Handler添加到链表tail前面。
                                                    private void addLast0(AbstractChannelHandlerContext newCtx) {
                                                        AbstractChannelHandlerContext prev = tail.prev;
                                                        newCtx.prev = prev;
                                                        newCtx.next = tail;
                                                        prev.next = newCtx;
                                                        tail.prev = newCtx;
                                                    }
                        -->config().group()// config()=ServerBootstrapConfig      
                            -->AbstractBootstrapConfig.group()
                                -->bootstrap.group();// bootstrap=ServerBootstrap; group()=NioEventLoopGroup
                        // 通过ServerBootstrap的bossGroup注册NioServerSocketChannel
                        -->NioEventLoopGroup.register(channel);// channel=NioServerSocketChannel  
                            -->next()// NioEventLoop
                            -->next().register(channel);  
                                -->register(Channel channel)// SingleThreadEventLoop
                        // 最后返回这个异步执行的占位符regFuture       
                        -->return regFuture;   
                    -->doBind0(regFuture, channel, localAddress, promise);
                        -->channel.bind(localAddress, promise)// NioServerSocketChannel
                            --> bind(SocketAddress localAddress, ChannelPromise promise)//AbstractChannel
                                -->pipeline.bind(localAddress, promise);// pipeline=DefaultChannelPipeline
                                    -->tail.bind(localAddress, promise);
                                        -->bind(final SocketAddress localAddress, final ChannelPromise promise)// AbstractChannelHandlerContext
                                            -->next.invokeBind(localAddress, promise);// DefaultChannelHandlerContext
                                                -->invokeBind(SocketAddress localAddress, ChannelPromise promise)// AbstractChannelHandlerContext
                                                    -->((ChannelOutboundHandler) handler()).bind(this, localAddress, promise);
                                                        -->bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)//LoggingHandler
                                                            -->ctx.bind(localAddress, promise);// DefaultChannelHandlerContext
                                                                -->bind(final SocketAddress localAddress, final ChannelPromise promise)// AbstractChannelHandlerContext
                                                                    -->next.invokeBind(localAddress, promise);// next=DefaultChannelHandlerContext$HeadContext
                                                                        -->invokeBind(SocketAddress localAddress, ChannelPromise promise)// AbstractChannelHandlerContext
                                                                            -->((ChannelOutboundHandler) handler()).bind(this, localAddress, promise);// DefaultChannelPipeline$HeadContext
                                                                                -->unsafe.bind(localAddress, promise);// AbstractChannel$AbstractUnsafe
                                                                                    -->doBind(localAddress);
                                                                                        // jdk底层的channel绑定端口的逻辑
                                                                                        -->javaChannel().bind(localAddress, config.getBacklog());
                                                                                -->pipeline.fireChannelActive();// pipeline=DefaultChannelPipeline传输active事件 
                                                                                    -->AbstractChannelHandlerContext.invokeChannelActive(head);
                                                                                        -->next.invokeChannelActive();// DefaultChannelPipeline$HeadContext
                                                                                            -->((ChannelInboundHandler) handler()).channelActive(this);
                                                                                                -->readIfIsAutoRead();
                                                                                                    -->channel.read();// channel=NioServerSocketChannel
                                                                                                        -->pipeline.read();// pipeline=DefaultChannelPipeline
                                                                                                            -->tail.read();
                                                                                                                -->next.invokeRead();
                                                                                                                    -->((ChannelOutboundHandler) handler()).read(this);// LoggingHandler
                                                                                                                        -->ctx.read();
                                                                                                                            -->next.invokeRead();
                                                                                                                                -->((ChannelOutboundHandler) handler()).read(this);
                                                                                                                                    -->unsafe.beginRead();
                                                                                                                                        -->doBeginRead();
                                                                                                                                            -->super.doBeginRead();
                                                                                                                                            protected void doBeginRead() throws Exception {
                                                                                                                                                // 拿到SelectionKey
                                                                                                                                                final SelectionKey selectionKey = this.selectionKey;
                                                                                                                                                if (!selectionKey.isValid()) {
                                                                                                                                                    return;
                                                                                                                                                }
                                                                                                                                        
                                                                                                                                                readPending = true;
                                                                                                                                                // 获取感兴趣的事件，channel注册的时候没有注册任何事件，所以此处是0
                                                                                                                                                final int interestOps = selectionKey.interestOps();
                                                                                                                                                // 判断是不是对任何事件都不监听
                                                                                                                                                if ((interestOps & readInterestOp) == 0) {
                                                                                                                                                    // 此条件成立，将之前的accept(16)事件注册, readInterest代表可以读取一个新连接的意思
                                                                                                                                                    // 注册完accept事件之后, 就可以轮询selector, 监听是否有新连接接入了 
                                                                                                                                                    selectionKey.interestOps(interestOps | readInterestOp);
                                                                                                                                                }
                                                                                                                                            }
                                                                                            
                                                                                            
                                                                                
                                                                    
                                                        
                                
```