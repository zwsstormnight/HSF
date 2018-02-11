package cn.nj.storm.rpc.service.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EchoClient
{
    private final String host = "127.0.0.1";
    
    private final int port = 18089;
    
    public EchoClient()
    {
    }
    
    public void start()
        throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap().group(group);
            b.channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    public void initChannel(SocketChannel ch)
                        throws Exception
                    {
                        ch.pipeline().addLast(new EchoClientHandler());
                    }
                });
            
            ChannelFuture f = b.connect().sync(); //6
            f.channel().closeFuture().sync(); //7
        }
        finally
        {
            group.shutdownGracefully().sync(); //8
        }
    }
    
    public static void main(String[] args)
        throws Exception
    {
        new EchoClient().start();
    }
}
