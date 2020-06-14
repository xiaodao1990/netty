package com.jenkin.netty.nio.codec1;

import com.jenkin.netty.nio.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 我们自定义一个Handler，需要继承netty规定好的HandlerAdapter
 */
@SuppressWarnings("all")
public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MyDataInfo.MyMessage) {
            MyDataInfo.MyMessage myMessage = (MyDataInfo.MyMessage) msg;
            MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
            if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
                MyDataInfo.Student student = myMessage.getStudent();
                System.out.println("学生id="+student.getId()+", 学生姓名="+student.getName());
            } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
                MyDataInfo.Worker worker = myMessage.getWorker();
                System.out.println("工程师姓名="+worker.getName()+", 工程师年龄="+worker.getAge());
            } else {
                System.out.println("传输的类型不正确");
            }
        }
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存中，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端喵喵1\r\n", CharsetUtil.UTF_8));
        super.channelReadComplete(ctx);
    }

    /**
     * 处理异常，一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
