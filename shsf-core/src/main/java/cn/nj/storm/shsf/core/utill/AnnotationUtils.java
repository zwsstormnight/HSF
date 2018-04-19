package cn.nj.storm.shsf.core.utill;

import cn.nj.storm.shsf.core.annotation.RpcConsumerService;
import cn.nj.storm.shsf.core.annotation.RpcMethod;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    
    /**
     * <获取指定目录下的指定注解的类>
     *
     * @param packageName
     * @return
     */
    public static Map<String, Set<Class<?>>> getServices(String packageName)
    {
        Map<String, Set<Class<?>>> map = new HashMap<>(2);
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> providerClasses = reflections.getTypesAnnotatedWith(RpcProviderService.class);
        Set<Class<?>> consumerClasses = reflections.getTypesAnnotatedWith(RpcConsumerService.class);
        map.put(Constants.PROVIDER, providerClasses);
        map.put(Constants.CONSUMER, consumerClasses);
        return map;
    }
}
