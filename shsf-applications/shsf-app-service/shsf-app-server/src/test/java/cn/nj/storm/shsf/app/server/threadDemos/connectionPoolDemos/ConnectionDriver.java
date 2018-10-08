package cn.nj.storm.shsf.app.server.threadDemos.connectionPoolDemos;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/10/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConnectionDriver
{
    
    private static class ConnectionHandler implements InvocationHandler
    {
        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
        {
            if (method.getName().equals("commit"))
            {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            return null;
        }
    }

    public static final Connection createConnection()
    {
        return (Connection)Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(),
                new Class<?>[] {Connection.class},
                new ConnectionHandler());
    }
}
