package cn.nj.storm.rpc.service.nio.netty.hello.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

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
public class DefaultWriteHandler extends ChannelOutboundHandlerAdapter
{
    private String messages;

    public DefaultWriteHandler()
    {
        
    }
    
    public DefaultWriteHandler(String msg)
    {
        this.messages = msg;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
    {
        System.out.println(this.getClass().getSimpleName() + " write");
//        byte[] req = messages.getBytes();//消息
//        ByteBuf firstMessage = Unpooled.buffer(req.length);//发送类
//        firstMessage.writeBytes(messages);//发送
        ctx.writeAndFlush(messages);//flush
        ReferenceCountUtil.release(msg);
        promise.setSuccess();
    }
}
