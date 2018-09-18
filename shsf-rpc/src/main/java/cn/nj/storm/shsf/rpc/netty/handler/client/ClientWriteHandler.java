package cn.nj.storm.shsf.rpc.netty.handler.client;

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
 * @version [版本号, 2018/2/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@ChannelHandler.Sharable
public class ClientWriteHandler extends ChannelOutboundHandlerAdapter
{
    private int isd = 0;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
    {
        System.out.println(this.getClass().getSimpleName() + " write");
        ctx.alloc().buffer().clear();
        ctx.writeAndFlush((String)msg + isd++);
        ReferenceCountUtil.release(msg);
        promise.setSuccess();
    }
}
