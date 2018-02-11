package cn.nj.storm.rpc.service.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.xml.datatype.XMLGregorianCalendar;
import java.net.InetSocketAddress;
import java.sql.Timestamp;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/9]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EchoServer
{
    private final int port = 18089;
    
    public EchoServer()
    {
    }
    
    public static void main(String[] args)
        throws Exception
    {
        new EchoServer().start();
    }
    
    /**
     * 这里start()的方式就是Reactor单线程模型
     * @throws Exception
     */
    public void start()
        throws Exception
    {
        //创建EventLoopGroup 接受与处理新的连接
        NioEventLoopGroup group = new NioEventLoopGroup();
        try
        {
            //创建服务端的bootstrap
            ServerBootstrap b = new ServerBootstrap().group(group);
            //指定NIO传输channel为NioServerSocketChannel：信道类型,设定端口，
            b.channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port)).childHandler(
                new ChannelInitializer<SocketChannel>()
                {
                    //初始化EchoServerHandler对象到SocketChannel的ChannelPipeline
                    @Override
                    public void initChannel(SocketChannel ch)
                        throws Exception
                    {
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });
            //绑定的服务器;sync():等待服务器关闭
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            //关闭channel;sync():等待关闭
            f.channel().closeFuture().sync();
        }
        finally
        {
            //关闭NioEventLoopGroup
            group.shutdownGracefully().sync();
        }
    }
    
}
