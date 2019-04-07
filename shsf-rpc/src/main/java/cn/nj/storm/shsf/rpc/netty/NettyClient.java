package cn.nj.storm.shsf.rpc.netty;

import cn.nj.storm.shsf.rpc.AbstractRpcClient;
import cn.nj.storm.shsf.rpc.netty.handler.client.ClientReadHandler;
import cn.nj.storm.shsf.rpc.netty.handler.client.ClientWriteHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NettyClient extends AbstractRpcClient
{
    
    private String address;
    
    private Integer port;
    
    private SocketAddress remoteAddress;
    
    public NettyClient()
    {
        
    }
    
    public NettyClient(String address, Integer port)
    {
        this.address = address;
        this.port = port;
        remoteAddress = new InetSocketAddress(address, port);
    }
    
    public void start()
        throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap().group(group);
            b.channel(NioSocketChannel.class)
                .remoteAddress(remoteAddress)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    public void initChannel(SocketChannel ch)
                        throws Exception
                    {
                        ChannelPipeline cp = ch.pipeline();
                        cp.addLast("decoder", new StringDecoder());
                        cp.addLast("encoder", new StringEncoder());
                        //ChannelOutboundHandler 在注册的时候需要放在最后一个ChannelInboundHandler之前，否则将无法传递到ChannelOutboundHandler。
                        cp.addLast(new ClientWriteHandler());
                        cp.addLast(new ClientReadHandler("callback"));
                    }
                });
            
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully().sync();
        }
    }
}