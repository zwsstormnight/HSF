package cn.nj.storm.shsf.rpc.netty;

import cn.nj.storm.shsf.rpc.netty.handler.client.ClientReadHandler;
import cn.nj.storm.shsf.rpc.netty.handler.client.ClientWriteHandler;
import cn.nj.storm.shsf.rpc.netty.handler.client.NettyClientHandler;
import com.google.common.base.Splitter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <RPC的连接池管理>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class ConnectManager
{
    /**
     * 连接池
     */
    private ConcurrentMap<String, NettyClient> connections = null;
    
    private CopyOnWriteArrayList<NettyClientHandler> connectedHandlers = new CopyOnWriteArrayList<>();
    
    private Map<InetSocketAddress, NettyClientHandler> connectedServerNodes = new ConcurrentHashMap<>();
    
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    
    private ReentrantLock lock = new ReentrantLock();
    
    private Condition connected = lock.newCondition();
    
    private long connectTimeoutMillis = 6000;
    
    private AtomicInteger roundRobin = new AtomicInteger(0);
    
    private volatile boolean isRuning = true;
    
    private static class SingletonHolder
    {
        public final static ConnectManager INSTANCE = new ConnectManager();
    }
    
    private ConnectManager()
    {
        if (connections == null)
        {
            connections = new ConcurrentHashMap<>(8);
        }
    }
    
    public static ConnectManager newInstance()
    {
        return SingletonHolder.INSTANCE;
    }
    
    /**
     * 更新连接
     * @param url
     */
    public void updateConnections(String url)
    {
        if (connections.get(url) == null)
        {
            List<String> w = Splitter.on(':').trimResults().omitEmptyStrings().splitToList(url);
            final InetSocketAddress remoteAddress = new InetSocketAddress(w.get(0), Integer.parseInt(w.get(1)));
            connectServerNode(remoteAddress);
        }
        //update local serverNodes cache
        HashSet<InetSocketAddress> newAllServerNodeSet = new HashSet<InetSocketAddress>();
        // Close and remove invalid server nodes
        for (NettyClientHandler connectedServerHandler : connectedHandlers)
        {
            SocketAddress remotePeer = connectedServerHandler.getRemoteAddress();
            if (!newAllServerNodeSet.contains(remotePeer))
            {
                log.info("Remove invalid server node " + remotePeer);
                NettyClientHandler handler = connectedServerNodes.get(remotePeer);
                if (handler != null)
                {
                    handler.close();
                }
                connectedServerNodes.remove(remotePeer);
                connectedHandlers.remove(connectedServerHandler);
            }
        }
    }
    
    public NettyClientHandler chooseHandler(String beanName)
    {
        return null;
    }
    
    private void connectServerNode(InetSocketAddress remoteAddress)
    {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel ch)
                    throws Exception
            {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("decoder", new StringDecoder());
                cp.addLast("encoder", new StringEncoder());
                cp.addLast(new NettyClientHandler());
            }
        });
        
        ChannelFuture channelFuture = b.connect(remoteAddress);
        channelFuture.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(final ChannelFuture channelFuture)
                throws Exception
            {
                if (channelFuture.isSuccess())
                {
                    log.debug("Successfully connect to remote server. remote peer = " + remoteAddress);
                    NettyClientHandler handler = channelFuture.channel().pipeline().get(NettyClientHandler.class);
                    addHandler(handler);
                }
            }
        });
    }
    
    private void addHandler(NettyClientHandler handler)
    {
        connectedHandlers.add(handler);
        InetSocketAddress remoteAddress = (InetSocketAddress)handler.getChannel().remoteAddress();
        connectedServerNodes.put(remoteAddress, handler);
        signalAvailableHandler();
    }
    
    private synchronized void signalAvailableHandler()
    {
        connected.signalAll();
    }
    
    private synchronized boolean waitingForHandler()
        throws InterruptedException
    {
        return connected.await(this.connectTimeoutMillis, TimeUnit.MILLISECONDS);
    }
    
    public void stop()
    {
        isRuning = false;
        for (int i = 0; i < connectedHandlers.size(); ++i)
        {
            NettyClientHandler connectedServerHandler = connectedHandlers.get(i);
            connectedServerHandler.close();
        }
        signalAvailableHandler();
        eventLoopGroup.shutdownGracefully();
    }
}
