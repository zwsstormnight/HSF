//package cn.nj.storm.shsf.common.utils;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
//import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinitionHolder;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.*;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.AnnotationBeanNameGenerator;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
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
//@Component
//public class BeanUtils implements BeanDefinitionRegistryPostProcessor
//{
//
//    private static ApplicationContext applicationContext;
//
//    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
//
//    /**
//     * 实例化时自动执行,通常用反射包获取到需要动态创建的接口类，容器初始化时，此方法执行，创建bean
//     * 执行过程与registryBeanWithDymicEdit基本一致
//     * @param beanDefinitionRegistry
//     * @throws BeansException
//     */
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry)
//        throws BeansException
//    {
//        List<Class<?>> beanClazzs = null;//反射获取需要代理的接口的clazz列表
//        //        for (Class beanClazz : beanClazzs) {
//        //            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
//        //            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
//        //            definition.getPropertyValues().add("interfaceClass", beanClazz);
//        //            definition.getPropertyValues().add("params", "注册传入工厂的参数，一般是properties配置的信息");
//        //            definition.setBeanClass(InterfaceFactoryBean.class);
//        //            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
//        //            beanDefinitionRegistry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
//        //        }
//    }
//
//    /**
//     * 实例化时自动执行
//     * @param configurableListableBeanFactory
//     * @throws BeansException
//     */
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
//        throws BeansException
//    {
//
//    }
//
//    //    public static Object getBean(Class<?> clazz){
//    //        return applicationContext.getBean(clazz);
//    //    }
//    //
//    //    public static Object getBean(String className){
//    //        return applicationContext.getBean(className);
//    //    }
//    //    /**
//    //     * 直接创建bean，不设置属性
//    //     * @param beanId
//    //     * @param clazz
//    //     * @return
//    //     */
//    //    public static boolean registryBean(String beanId, Class<?> clazz){
//    //        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
//    //        BeanDefinition definition = builder.getBeanDefinition();
//    //        getRegistry().registerBeanDefinition(beanId, definition);
//    //        return true;
//
//
//
//
//
////    AnnotatedBeanDefinition annotatedBeanDefinition  = new AnnotatedGenericBeanDefinition(beanClass);
////         //可以自动生成name
////               String beanName = (name != null?name:this.beanNameGenerator.generateBeanName(annotatedBeanDefinition, registry));
////       //bean注册的holer类.
////               BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(annotatedBeanDefinition, beanName);
////       //使用bean注册工具类进行注册.
////               BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
//    //    }
//
//    public boolean registry(String beanId, Class<?> clazz)
//    {
//        AnnotatedBeanDefinition annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(clazz);
//        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(annotatedBeanDefinition)
//        return true;
//    }
//
//    //    /**
//    //     * 为已知的class创建bean，可以设置bean的属性，可以用作动态代理对象的bean扩展
//    //     * @param beanId
//    //     * @param
//    //     * @return
//    //     */
//    //    public static boolean registryBeanWithEdit(String beanId, Class<?> factoryClazz, Class<?> beanClazz){
//    //        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
//    //        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
//    //        definition.getPropertyValues().add("myClass", beanClazz);
//    //        definition.setBeanClass(factoryClazz);
//    //        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
//    //        getRegistry().registerBeanDefinition(beanId, definition);
//    //        return true;
//    //    }
//    //
//    //    /**
//    //     * 为已知的class创建bean，可以设置bean的属性，可以用作动态代理对象的bean扩展
//    //     * @param beanId
//    //     * @param
//    //     * @return
//    //     */
//    //    public static boolean registryBeanWithDymicEdit(String beanId, Class<?> factoryClazz, Class<?> beanClazz, String params){
//    //        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
//    //        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
//    //        definition.getPropertyValues().add("interfaceClass", beanClazz);
//    //        definition.getPropertyValues().add("params", params);
//    //        definition.setBeanClass(factoryClazz);
//    //        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
//    //        getRegistry().registerBeanDefinition(beanId, definition);
//    //        return true;
//    //    }
//    //
//    //    /**
//    //     * 获取注册者
//    //     * context->beanfactory->registry
//    //     * @return
//    //     */
//    //    public static BeanDefinitionRegistry getRegistry(){
//    //        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
//    //        return (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
//    //    }
//}