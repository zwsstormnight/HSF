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
            /**
             * Netty 中配置程序的过程，当你需要连接客户端或服务器绑定指定端口时需要引导：创建服务端的bootstrap-->ServerBootstrap
             * 监听在服务器某个指定端口并轮询客户端的Bootstrap或DatagramChannel是否连接服务器。
             *
             *
             */
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
                        /**
                         * ChannelHandler:一个通用的容器，处理进来的事件（包括数据）并且通过ChannelPipeline
                         *
                         * 为了使数据从一端到达另一端，一个或多个 ChannelHandler 将以某种方式操作数据。
                         * 这些 ChannelHandler 会在程序的“引导”阶段被添加ChannelPipeline中，并且被添加的顺序将决定处理数据的顺序
                         */
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });
            /**
             *
             * 绑定的服务器;sync():等待服务器响应
             * 通常需要调用“Bootstrap”类的connect()方法，但是也可以先调用bind()再调用connect()进行连接，之后使用的Channel包含在bind()返回的ChannelFuture中。
             */
            ChannelFuture f = b.bind().sync();
            /**
             * 一个 ServerBootstrap 可以认为有2个 Channel 集合，
             * 第一个集合包含一个单例 ServerChannel，代表持有一个绑定了本地端口的 socket；
             * 第二集合包含所有创建的 Channel，处理服务器所接收到的客户端进来的连接
             */
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            //关闭channel;sync():等待响应
            f.channel().closeFuture().sync();
        }
        finally
        {
            //关闭NioEventLoopGroup
            group.shutdownGracefully().sync();
        }
    }
    
}
