package cn.nj.storm.shsf.core.register.helper;

import cn.nj.storm.shsf.core.annotation.RpcConsumerService;
import cn.nj.storm.shsf.core.annotation.RpcMethod;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.utils.AnnotationUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
    public static Map<String, List<ServiceConfig>> scannerInterfaces(String packageName)
    {
        Map<String, List<ServiceConfig>> serviceConfigs = Maps.newHashMap();
        List<RpcProviderService> providers =
            AnnotationUtils.getAnnotationInstanceOnType(packageName, RpcProviderService.class);
        List<RpcConsumerService> consumers =
            AnnotationUtils.getAnnotationInstanceOnField(packageName, RpcConsumerService.class);
        if (CollectionUtils.isEmpty(providers) && CollectionUtils.isEmpty(consumers))
        {
            return serviceConfigs;
        }
        
        List<ServiceConfig> providerList = providers.stream().map(rpcProviderService -> {
            ServiceConfig serviceConfig = new ServiceConfig();
            //接口类别
            serviceConfig.setServiceType(RpcProviderService.SERVICE_TYPE);
            //接口名称
            serviceConfig.setInterfaceName(rpcProviderService.interfaceClass().getName());
            //接口代理名称
            serviceConfig.setName(rpcProviderService.name());
            //接口类
            serviceConfig.setInterfaceClass(rpcProviderService.interfaceClass());
            //实现类
            serviceConfig.setImplementClass(rpcProviderService.value());
            serviceConfig.setRetries(rpcProviderService.retries());
            serviceConfig.setTimeout(rpcProviderService.timeout());
            return serviceConfig;
        }).collect(Collectors.toList());
        serviceConfigs.put(RpcProviderService.SERVICE_TYPE, providerList);
        
        List<ServiceConfig> consumerList = consumers.stream().map(rpcConsumerService -> {
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setServiceType(RpcConsumerService.SERVICE_TYPE);
            serviceConfig.setName(rpcConsumerService.name());
            //接口类
            serviceConfig.setInterfaceClass(rpcConsumerService.interfaceClass());
            serviceConfig.setInterfaceName(rpcConsumerService.interfaceClass().getName());
            serviceConfig.setRetries(rpcConsumerService.retries());
            serviceConfig.setTimeout(rpcConsumerService.timeout());
            return serviceConfig;
        }).collect(Collectors.toList());
        serviceConfigs.put(RpcConsumerService.SERVICE_TYPE, consumerList);
        return serviceConfigs;
    }
    
    /**
     * <扫描接口中的方法>
     * @param services
     * @return
     */
    public static Map<String, Set<MethodConfig>> scannerMethods(List<ServiceConfig> services)
    {
        Map<String, Set<MethodConfig>> methodMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(services))
        {
            for (ServiceConfig serviceConfig : services)
            {
                Set<MethodConfig> methodConfigs = RegisterHelper.getMethods(serviceConfig);
                serviceConfig.setMethodConfigs(methodConfigs);
                methodMap.put(serviceConfig.getName(), methodConfigs);
            }
        }
        return methodMap;
    }
    
    private static Set<MethodConfig> getMethods(ServiceConfig serviceConfig)
    {
        Set<MethodConfig> methodConfigSet = new HashSet<>();
        Set<MethodConfig> annoedMethods = new HashSet<>();
        Method[] methods = serviceConfig.getImplementClass().getDeclaredMethods();
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
    
    /**
     * 解析注册节点的信息
     * @param dataStr
     */
    public static ServiceConfig explainInfo(String dataStr)
    {
        if (StringUtils.isBlank(dataStr))
        {
            return null;
        }
        List<String> pathInfos = Splitter.on('&').trimResults().omitEmptyStrings().splitToList(dataStr);
        ServiceConfig serviceConfig = new ServiceConfig();
        for (String info : pathInfos)
        {
            List<String> pairs = Splitter.on('=').trimResults().omitEmptyStrings().splitToList(info);
            Field field;
            try
            {
                field = ServiceConfig.class.getDeclaredField(pairs.get(0));
                field.setAccessible(true);
                field.set(serviceConfig, pairs.get(1));
            }
            catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException ex)
            {
                ex.printStackTrace();
            }
        }
        return serviceConfig;
    }
    
}