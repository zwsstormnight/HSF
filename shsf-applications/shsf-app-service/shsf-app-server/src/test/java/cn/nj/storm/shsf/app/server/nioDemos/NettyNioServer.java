package cn.nj.storm.shsf.app.server.nioDemos;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * <通过Netty封装实现NIO服务端处理>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/13]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NettyNioServer
{
    public void server(int port)
        throws Exception
    {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        NioEventLoopGroup group = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    public void initChannel(SocketChannel ch)
                        throws Exception
                    {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter()
                        {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg)
                            {
                                //将消息缓冲
                                ByteBuf in = (ByteBuf)msg;
                                System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
                                //将所接收的消息返回给发送者 ctx.write(in);
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx)
                                throws Exception
                            {
                                //写信息到客户端，并添加 ChannelFutureListener 当一旦消息写入就关闭连接
                                ChannelFuture cf = ctx.writeAndFlush(buf.duplicate());
                                cf.addListener(ChannelFutureListener.CLOSE);
                            }
                        });
                    }
                });
            ChannelFuture f = b.bind().sync();
            System.out.println(NettyNioServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)
            throws Exception
    {
        new NettyNioServer().server(18089);
    }
}
