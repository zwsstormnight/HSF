package cn.nj.storm.rpc.service.nio.netty.hello.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/22]
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
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        System.out.println(this.getClass().getSimpleName() + " channelActive");
        ctx.fireChannelActive();
        //将已获得的连接告知客户端
        ctx.write(messages);
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
            ctx.write(byteBuf);
        }
        else
        {
            m = m + msg;
            //将所接收的消息返回给发送者
            ctx.write(msg);
        }
        System.out.println(m);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
