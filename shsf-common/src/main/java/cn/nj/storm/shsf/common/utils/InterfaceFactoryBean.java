//package cn.nj.storm.shsf.common.utils;
//
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
//    private Class<T> interfaceClass;
//
//    /**
//     * 在bean注册时设置
//     */
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