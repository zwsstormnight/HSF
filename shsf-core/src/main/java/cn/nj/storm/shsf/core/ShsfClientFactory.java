package cn.nj.storm.shsf.core;

import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.utils.Constants;
import cn.nj.storm.shsf.core.utils.JavassistUtils;
import cn.nj.storm.shsf.rpc.netty.ConnectManager;
import cn.nj.storm.shsf.rpc.proxy.ApiProxy;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ShsfClientFactory
{
    private static ThreadPoolExecutor threadPoolExecutor = null;
    
    /**
     * 服务发现的结果
     */
    private RegisterService registerService;
    
    public ShsfClientFactory(RegisterService registerService)
    {
        this.registerService = registerService;
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));
    }
    
    /**
     * 初始化服务消费端：
     * 1.接口执行的动态代理构建
     * 由于对象实现了若干接口，spring使用JDK的java.lang.reflect.Proxy类代理。
     * 2.服务通讯：长连接，短连接
     */
    public void init(DefaultListableBeanFactory beanFactory)
    {
        //遍历消费者
        for (ServiceConfig serviceConfig : this.registerService.getServiceCaches().get(Constants.CONSUMER))
        {
            if (serviceConfig.getImplementClass() == null)
            {
                //这里是jdk的动态代理实现方式
                //                JavassistUtils.createClass(serviceConfig)
                Object implementClass = create(serviceConfig.getInterfaceClass(),
                    serviceConfig.getName(),
                    serviceConfig.getImplementName());
                serviceConfig.setImplementClass(implementClass.getClass());
                beanFactory.applyBeanPostProcessorsAfterInitialization(implementClass, serviceConfig.getName());
                beanFactory.registerSingleton(serviceConfig.getName(), implementClass);
            }
            ConnectManager manager = ConnectManager.newInstance();
            for (String url : registerService.getConsumerLists().get(serviceConfig.getInterfaceName()))
            {
                manager.updateConnections(url);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass, String beanName, String className)
    {
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
            new Class<?>[] {interfaceClass},
            new ApiProxy<>(interfaceClass, beanName, className, threadPoolExecutor));
    }
    
    public static void submit(Runnable task)
    {
        threadPoolExecutor.submit(task);
    }
    
    public static void submit(Callable task)
    {
        threadPoolExecutor.submit(task);
    }
}
