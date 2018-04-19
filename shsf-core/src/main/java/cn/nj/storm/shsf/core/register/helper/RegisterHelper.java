package cn.nj.storm.shsf.core.register.helper;

import cn.nj.storm.shsf.core.annotation.RpcConsumerService;
import cn.nj.storm.shsf.core.annotation.RpcMethod;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.utill.AnnotationUtils;
import cn.nj.storm.shsf.core.utill.Constants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <方法注册的工具类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RegisterHelper
{
    private RegisterHelper()
    {
        
    }
    
    /**
     * <扫描接口中的方法>
     * @param packageName 扫描的包目录
     * @return
     */
    public static Set<ServiceConfig> scannerInterfaces(String packageName)
    {
        Set<ServiceConfig> serviceConfigs = new HashSet<>();
        Map<String, Set<Class<?>>> map = AnnotationUtils.getServices(packageName);
        if (MapUtils.isEmpty(map))
        {
            return serviceConfigs;
        }
        for (Class<?> serviceClass : map.get(Constants.PROVIDER))
        {
            RpcProviderService rpcProviderService = serviceClass.getAnnotation(RpcProviderService.class);
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setServiceType(Constants.PROVIDER);
            serviceConfig.setName(rpcProviderService.name());
            serviceConfig.setValueName(rpcProviderService.value().getSimpleName());
            serviceConfig.setClazz(rpcProviderService.value());
            serviceConfig.setRetries(rpcProviderService.retries());
            serviceConfig.setTimeout(rpcProviderService.timeout());
            serviceConfigs.add(serviceConfig);
        }
        for (Class<?> serviceClass : map.get(Constants.CONSUMER))
        {
            RpcConsumerService rpcConsumerService = serviceClass.getAnnotation(RpcConsumerService.class);
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setServiceType(Constants.CONSUMER);
            serviceConfig.setName(rpcConsumerService.name());
            serviceConfig.setValueName(rpcConsumerService.value().getSimpleName());
            serviceConfig.setClazz(rpcConsumerService.value());
            serviceConfig.setRetries(rpcConsumerService.retries());
            serviceConfig.setTimeout(rpcConsumerService.timeout());
            serviceConfigs.add(serviceConfig);
        }
        return serviceConfigs;
    }
    
    /**
     * <扫描接口中的方法>
     * @param services
     * @return
     */
    public static Map<String, Set<MethodConfig>> scannerMethods(Set<ServiceConfig> services)
    {
        Map<String, Set<MethodConfig>> methodMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(services))
        {
            for (ServiceConfig serviceConfig : services)
            {
                Set<MethodConfig> methodConfigs = RegisterHelper.getMethods(serviceConfig);
                methodMap.put(serviceConfig.getName(), methodConfigs);
            }
        }
        return methodMap;
    }
    
    private static Set<MethodConfig> getMethods(ServiceConfig serviceConfig)
    {
        Set<MethodConfig> methodConfigSet = new HashSet<>();
        Set<MethodConfig> annoedMethods = new HashSet<>();
        Method[] methods = serviceConfig.getClazz().getMethods();
        int methodSize = methods.length;
        if (methodSize > 0)
        {
            for (Method mt : methods)
            {
                MethodConfig methodConfig = new MethodConfig();
                methodConfig.setName(mt.getName());
                methodConfig.setReturnType(mt.getReturnType());
                if (mt.getAnnotation(RpcMethod.class) != null)
                {
                    annoedMethods.add(methodConfig);
                }
                methodConfigSet.add(methodConfig);
            }
        }
        return CollectionUtils.isEmpty(annoedMethods) ? methodConfigSet : annoedMethods;
    }
}
