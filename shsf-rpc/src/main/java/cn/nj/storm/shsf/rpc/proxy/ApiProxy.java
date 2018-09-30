package cn.nj.storm.shsf.rpc.proxy;

import cn.nj.storm.shsf.rpc.async.ShsfFutureResponse;
import cn.nj.storm.shsf.rpc.netty.ConnectManager;
import cn.nj.storm.shsf.rpc.netty.handler.client.NettyClientHandler;
import cn.nj.storm.shsf.rpc.protocal.ShsfRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
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
public class ApiProxy<T> implements InvocationHandler, AsyncExecuteProxy
{
    private Class<T> interfaceClass;
    
    private String beanName;
    
    private String className;
    
    private ThreadPoolExecutor threadPoolExecutor;
    
    public ApiProxy(Class<T> interfaceClass, String beanName, String className)
    {
        this.interfaceClass = interfaceClass;
        this.beanName = beanName;
        this.className = className;
    }
    
    public ApiProxy(Class<T> interfaceClass, String beanName, String className, ThreadPoolExecutor threadPoolExecutor)
    {
        this.interfaceClass = interfaceClass;
        this.beanName = beanName;
        this.className = className;
        this.threadPoolExecutor = threadPoolExecutor;
    }
    
    @Override
    public ShsfFutureResponse call(String funcName, Object... args)
    {
        //获取通道
        NettyClientHandler handler = ConnectManager.newInstance().chooseHandler(this.beanName);
        //构建请求体
        ShsfRequest request = createRequest(this.interfaceClass.getName(), funcName, args);
        ShsfFutureResponse futureResponse = handler.sendRequest(request, threadPoolExecutor);
        return futureResponse;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        if (Object.class == method.getDeclaringClass())
        {
            String name = method.getName();
            if ("equals".equals(name))
            {
                return proxy == args[0];
            }
            else if ("hashCode".equals(name))
            {
                return System.identityHashCode(proxy);
            }
            else if ("toString".equals(name))
            {
                return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy))
                    + ", with InvocationHandler " + this;
            }
            else
            {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        //填充整理报文
        ShsfRequest request = new ShsfRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        //确认网络位置
        NettyClientHandler handler = ConnectManager.newInstance().chooseHandler(this.beanName);
        //序列化,网络传输，反序列化，返回结果
        ShsfFutureResponse rpcFuture = handler.sendRequest(request, threadPoolExecutor);
        return rpcFuture.get();
    }
    
    private ShsfRequest createRequest(String className, String funcName, Object... args)
    {
        ShsfRequest request = new ShsfRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setBeanName(beanName);
        request.setClassName(className);
        request.setMethodName(funcName);
        //        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        return request;
    }
}
