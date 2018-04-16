package cn.nj.storm.rpc.service.nio.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>
{
    /**
     * 在接收到数据时被调用。注意，由服务器所发送的消息可以以块的形式被接收。
     * 即，当服务器发送 5 个字节是不是保证所有的 5 个字节会立刻收到 - 即使是只有 5 个字节，channelRead0() 方法可被调用两次，
     * 第一次用一个ByteBuf（Netty的字节容器）装载3个字节和第二次一个 ByteBuf 装载 2 个字节。
     * 唯一要保证的是，该字节将按照它们发送的顺序分别被接收。 （注意，这是真实的，只有面向流的协议如TCP）
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
        throws Exception
    {
        //收到消息
        System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 建立连接后该 channelActive() 方法被调用一次。逻辑很简单：一旦建立了连接，字节序列被发送到服务器。
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        //当被通知该 channel 是活动的时候就发送信息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
