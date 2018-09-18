package cn.nj.storm.shsf.rpc.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * <netty-server 默认的流读取处理器>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@ChannelHandler.Sharable
public class DefaultReadHandler extends ChannelInboundHandlerAdapter
{
    private String messages;

    public DefaultReadHandler(String msg)
    {
        this.messages = msg;
    }

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println(this.getClass().getSimpleName() + " channelActive");
        //ChannelInboundHandler之间的传递，通过调用 ctx.fireChannelRead(msg) 实现
        ctx.fireChannelActive();
        //调用ctx.write(msg) 将传递到ChannelOutboundHandler
        ctx.write(messages);
        //ctx.write()方法执行后，需要调用flush()方法才能令它立即执行
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println(this.getClass() + " channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        String m = "system received: ";
        if (msg instanceof ByteBuf)
        {
            //将消息缓冲
            ByteBuf byteBuf = (ByteBuf)msg;
            m = m + byteBuf.toString(CharsetUtil.UTF_8);
            //将所接收的消息返回给发送者
            ctx.writeAndFlush(byteBuf);
        }
        else
        {
            m = m + msg;
            //将所接收的消息返回给发送者
            ctx.writeAndFlush(msg);
        }
        System.out.println(m);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println(this.getClass().getSimpleName() + " channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }

    private String ctxMessages(ChannelHandlerContext ctx)
    {
        try
        {
            ByteBuf byteBuf = ctx.alloc().buffer();
            byte[] array = null;
            if (!byteBuf.hasArray())
            {
                int length = byteBuf.readableBytes();
                array = new byte[length];
                byteBuf.getBytes(byteBuf.readerIndex(), array);
            }
            else
            {
                array = byteBuf.array();
            }
            return byteBuf.toString(CharsetUtil.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
