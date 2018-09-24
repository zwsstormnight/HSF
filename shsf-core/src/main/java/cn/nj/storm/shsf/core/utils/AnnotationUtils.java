package cn.nj.storm.shsf.core.utils;

import cn.nj.storm.shsf.core.annotation.RpcConsumerService;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <注解工具类>
 * <依赖 >
 * <dependency>
 * <groupId>org.reflections</groupId>
 * <artifactId>reflections</artifactId>
 * <version>0.9.10</version>
 * </dependency>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AnnotationUtils
{
    private AnnotationUtils()
    {
        
    }
    
    /**
     * <获取指定目录下的指定注解的类>
     *
     * @param packageName
     * @return
     */
    public static Map<String, Set<Class<?>>> getServices(String packageName, Class<? extends Annotation>... annotations)
    {
        Map<String, Set<Class<?>>> map = new HashMap<>(2);
        Reflections reflections = new Reflections(packageName);
        //                Arrays.stream(annotations).map(annotation -> reflections.getTypesAnnotatedWith(annotation)).collect(
        //                    Collectors.toMap(Collectors.groupingBy(Annotation::SERVICE_TYPE));
        //获取服务提供者的列表
        Set<Class<?>> providerClasses = reflections.getTypesAnnotatedWith(RpcProviderService.class);
        //获取服务消费者的列表
        Set<Class<?>> consumerClasses = reflections.getTypesAnnotatedWith(RpcConsumerService.class);
        map.put(Constants.PROVIDER, providerClasses);
        map.put(Constants.CONSUMER, consumerClasses);
        return map;
    }
    
    /**
     * <获取指定目录下的指定注解的类>
     *
     * @param packageName
     * @return
     */
    public static <T extends Annotation> List<T> getAnnotationInstanceOnType(String packageName, Class<T> annotation)
    {
        Reflections reflections = new Reflections(packageName);
        //获取服务提供者的列表
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(annotation);
        return classSet.stream().map(clazz -> clazz.getAnnotation(annotation)).collect(Collectors.toList());
    }
    
    /**
     * 获取指定目录下指定注释的字段
     * @param packageName
     * @param annotation
     * @param <T>
     * @return
     */
    public static <T extends Annotation> List<T> getAnnotationInstanceOnField(String packageName, Class<T> annotation)
    {
        Reflections reflections = new Reflections(
            new ConfigurationBuilder().setScanners(new FieldAnnotationsScanner()).forPackages(packageName));
        Set<Field> fieldSet = reflections.getFieldsAnnotatedWith(RpcConsumerService.class);
        return fieldSet.stream().map(field -> field.getAnnotation(annotation)).collect(Collectors.toList());
    }
}
