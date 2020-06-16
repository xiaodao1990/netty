### Netty服务启动源码剖析
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
                            -->clazz.getConstructor().newInstance();// 实例化NioServerSocketChannel
                        -->init(channel);
                            -->ServerBootstrap.init(Channel channel)
```