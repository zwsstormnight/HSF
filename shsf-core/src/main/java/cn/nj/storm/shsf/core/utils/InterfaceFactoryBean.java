//package cn.nj.storm.shsf.core.utils;
//
//import cn.nj.storm.shsf.rpc.proxy.ApiProxy;
//import lombok.Data;
//import org.springframework.beans.factory.FactoryBean;
//
//import java.lang.reflect.Proxy;
//
///**
// * <一句话功能简述>
// * <功能详细描述>
// *
// * @author zhengweishun
// * @version [版本号, 2018/9/27]
// * @see [相关类/方法]
// * @since [产品/模块版本]
// */
//@Data
//public class InterfaceFactoryBean<T> implements FactoryBean<T>
//{
//
//    private Class<T> interfaceClass;
//
//    private String params;
//
//    /**
//     * 新建bean
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public T getObject()
//        throws Exception
//    {
//        //利用反射具体的bean新建实现，不支持T为接口。
//        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
//            new Class[] {interfaceClass},
//            new DymicInvocationHandler(params));
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> T create(Class<T> interfaceClass, String beanName, String className)
//    {
//        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
//                new Class<?>[] {interfaceClass},
//                new ApiProxy<>(interfaceClass, beanName, className, threadPoolExecutor));
//    }
//
//    /**
//     * 获取bean
//     * @return
//     */
//    @Override
//    public Class<?> getObjectType()
//    {
//        return interfaceClass;
//    }
//
//    @Override
//    public boolean isSingleton()
//    {
//        return true;
//    }
//
//}
