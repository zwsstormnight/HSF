package cn.nj.storm.shsf.rpc.netty;

import cn.nj.storm.shsf.rpc.netty.handler.DefaultReadHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NettyServer
{
    
    /**
     *
     * @param port
     */
    public void startServer(int port)
    {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap().group(group);
            b.channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ProviderChannelInitializer());
            ChannelFuture f = b.bind().sync();
            System.out.println(this.getClass().getName() + " started and listen on " + f.channel().localAddress());
            //关闭channel;sync():等待响应
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            group.shutdownGracefully();
        }
    }
}

class ProviderChannelInitializer extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel socketChannel)
        throws Exception
    {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast("decoder", new StringDecoder());
        p.addLast("encoder", new StringEncoder());
        p.addLast(new DefaultReadHandler("收到"));
        //        p.addLast(new DefaultWriteHandler("111111111"));
    }
}
