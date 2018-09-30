package cn.nj.storm.shsf.rpc.netty;

import cn.nj.storm.shsf.rpc.RpcServer;
import cn.nj.storm.shsf.rpc.netty.handler.DefaultReadHandler;
import cn.nj.storm.shsf.rpc.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <Netty 服务>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class NettyServer extends RpcServer
{
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    
    /**
     * <ip:port, channel>
     */
    private Map<String, Channel> channels;
    
    private ServerBootstrap bootstrap;
    
    private Channel channel;
    
    public NettyServer()
    {
    }
    
    /**
     * 启动netty服务
     * @param hostAddress
     * @param port
     */
    public void startServer(String hostAddress, int port)
    {
        if (bossGroup == null && workerGroup == null)
        {
            log.warn("start NettyServer ...");
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            final NettyServerHandler nettyServerHandler = new NettyServerHandler();
//            channels = nettyServerHandler.getChannels();
            try
            {
                ServerBootstrap b = new ServerBootstrap().group(bossGroup, workerGroup);
                b.channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ProviderChannelInitializer());
                ChannelFuture channelFuture = b.bind(hostAddress, port);
                channelFuture.syncUninterruptibly();
                channel = channelFuture.channel();
                log.warn(
                    this.getClass().getName() + " started and listen on " + channelFuture.channel().localAddress());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.stop();
                log.warn(this.getClass().getName() + " has stoped ...");
            }
        }
        
    }
    
    public void stop()
    {
        if (bossGroup != null)
        {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null)
        {
            workerGroup.shutdownGracefully();
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
