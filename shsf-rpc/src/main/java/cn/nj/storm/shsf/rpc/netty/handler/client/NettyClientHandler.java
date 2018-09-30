package cn.nj.storm.shsf.rpc.netty.handler.client;

import cn.nj.storm.shsf.rpc.async.ShsfFutureResponse;
import cn.nj.storm.shsf.rpc.protocal.ShsfRequest;
import cn.nj.storm.shsf.rpc.protocal.ShsfResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ShsfResponse>
{
    private volatile Channel channel;
    
    private ConcurrentHashMap<String, ShsfFutureResponse> processes = new ConcurrentHashMap<>();
    
    private SocketAddress remoteAddress;
    
    public Channel getChannel()
    {
        return channel;
    }
    
    public SocketAddress getRemoteAddress()
    {
        return remoteAddress;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        super.channelActive(ctx);
        this.remoteAddress = this.channel.remoteAddress();
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
        throws Exception
    {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ShsfResponse shsfResponse)
        throws Exception
    {
        String requestId = shsfResponse.getRequestId();
        ShsfFutureResponse futureResponse = processes.get(requestId);
        if (futureResponse != null)
        {
            processes.remove(requestId);
            futureResponse.done(shsfResponse);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        log.error("client caught exception", cause);
        ctx.close();
    }
    
    /**
     * 异步调用返回请求
     * @param request
     * @return
     */
    public ShsfFutureResponse sendRequest(ShsfRequest request, ThreadPoolExecutor threadPoolExecutor)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        ShsfFutureResponse futureResponse = new ShsfFutureResponse(request,threadPoolExecutor);
        processes.put(request.getRequestId(), futureResponse);
        channel.writeAndFlush(request).addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future)
            {
                latch.countDown();
            }
        });
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        return futureResponse;
    }
    
    /**
     * 同步调用返回请求
     * @param request
     * @return
     */
    public ShsfFutureResponse sendRequestSync(ShsfRequest request)
    {
        return null;
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
