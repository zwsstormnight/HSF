package cn.nj.storm.shsf.rpc.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/18]
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
        ctx.writeAndFlush(messages);
        ReferenceCountUtil.release(msg);
        promise.setSuccess();
    }
}
