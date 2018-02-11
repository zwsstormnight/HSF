package cn.nj.storm.rpc.service.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/9]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        //将消息缓冲
        ByteBuf in = (ByteBuf)msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        //将所接收的消息返回给发送者
        ctx.write(in);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception
    {
        //处理所有接收到的数据
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 每个 Channel 都有一个关联的 ChannelPipeline，它代表了 ChannelHandler 实例的链
     * ChannelInboundHandlerAdapter 适配器处理的实现只是将一个处理方法调用转发到链中的下一个处理器。
     * 因此，如果一个 Netty 应用程序不覆盖exceptionCaught ，那么这些错误将最终到达 ChannelPipeline，并且结束警告将被记录
     * 提供至少一个 实现 exceptionCaught 的 ChannelHandler
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        //关闭通道
        ctx.close();
    }
}
