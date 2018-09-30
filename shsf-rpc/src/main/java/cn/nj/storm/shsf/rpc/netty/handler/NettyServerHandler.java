package cn.nj.storm.shsf.rpc.netty.handler;

import cn.nj.storm.shsf.rpc.RpcServer;
import cn.nj.storm.shsf.rpc.protocal.ShsfRequest;
import cn.nj.storm.shsf.rpc.protocal.ShsfResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<ShsfRequest>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ShsfRequest msg)
        throws Exception
    {
//        RpcServer.submit(() -> {
            log.debug("Receive request " + msg.getRequestId());
            ShsfResponse response = new ShsfResponse();
            response.setRequestId(msg.getRequestId());
            try
            {
                response.setResult(handle(msg));
            }
            catch (Throwable t)
            {
                response.setError(t.toString());
                log.error("RPC Server handle request error", t);
            }
            ctx.writeAndFlush(response).addListener(
                (ChannelFutureListener)channelFuture -> log.debug("Send response for request " + msg.getRequestId()));
//        });
    }
    
    private Object handle(ShsfRequest request)
        throws Throwable
    {
        String className = request.getClassName();
//        Object serviceBean = handlerMap.get(className);
        
//        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        // JDK reflect
        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/
        
        // Cglib reflect
//        FastClass serviceFastClass = FastClass.create(serviceClass);
//        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
//        return serviceFastMethod.invoke(serviceBean, parameters);
        return null;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        log.error("server caught exception", cause);
        ctx.close();
    }
}
