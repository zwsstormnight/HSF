package cn.nj.storm.shsf.core.utill;

import cn.nj.storm.shsf.core.annotation.RpcMethod;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
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
public class AnnotationUtils {

    /**
     * <获取指定目录下的指定注解的类>
     *
     * @param packageName
     * @return
     */
    public static Set<ServiceConfig> getServices(String packageName, Class clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(clazz);
        Set<ServiceConfig> serviceConfigs = new HashSet<>();
        for (Class<?> serviceClass : classesList) {
            RpcProviderService rpcProviderService = (RpcProviderService) serviceClass.getAnnotation(clazz);
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setName(rpcProviderService.name());
//            serviceConfig.setValueName();
            serviceConfig.setClazz(rpcProviderService.value());
            serviceConfig.setRetries(rpcProviderService.retries());
            serviceConfig.setTimeout(rpcProviderService.timeout());
            serviceConfigs.add(serviceConfig);
        }
        return serviceConfigs;
    }

    public static Set<MethodConfig> getMethods(ServiceConfig serviceConfig) {
        Set<MethodConfig> methodConfigSet = new HashSet<>();
        Set<MethodConfig> annoedMethods = new HashSet<>();
        Method[] methods = serviceConfig.getClazz().getMethods();
        int methodSize = methods.length;
        if (methodSize > 0) {
            for (Method mt : methods) {
                MethodConfig methodConfig = new MethodConfig();
                methodConfig.setName(mt.getName());
                methodConfig.setReturnType(mt.getReturnType());
                if (mt.getAnnotation(RpcMethod.class) != null) {
                    annoedMethods.add(methodConfig);
                }
                methodConfigSet.add(methodConfig);
            }
        }
        return CollectionUtils.isEmpty(annoedMethods) ? methodConfigSet : annoedMethods;
    }
}
