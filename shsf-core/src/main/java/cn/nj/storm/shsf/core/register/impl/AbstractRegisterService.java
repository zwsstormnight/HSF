package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.helper.RegisterHelper;
import cn.nj.storm.shsf.core.utils.Constants;
import cn.nj.storm.shsf.core.utils.LoggerInterface;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type.NODE_REMOVED;

/**
 * <服务注册骨架类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public abstract class AbstractRegisterService implements RegisterService, LoggerInterface
{
    
    protected String namespace;
    
    protected String appAddress;
    
    /**
     * 存储当前订阅的接口的提供者的列表，方便在消费端做负载均衡
     * 如：
     * key= cn.nj.storm.shsf.app.api.service.IHappyService
     * value= [192.168.0.1:20881,192.168.0.2:20881,192.168.0.3:20881]
     */
    protected static ConcurrentMap<String, List<String>> consumersMap;
    
    /**
     * 订阅接口的路径
     */
    protected static List<String> consumerPaths;
    
    /**
     * 已扫描的本地服务的存储位置
     */
    protected static Map<String, List<ServiceConfig>> services;
    
    /**
     * 本地服务所涵方法的存储位置
     * key: interface是springbean的name
     * --- iHappyService
     */
    protected static Map<String, Set<MethodConfig>> serviceMethods;
    
    @Override
    public Map<String, List<ServiceConfig>> scanner(String packageName)
    {
        services = RegisterHelper.scannerInterfaces(packageName);
        if (MapUtils.isEmpty(services))
        {
            return services;
        }
        //对需要发布的服务扫描可用的方法
        serviceMethods = RegisterHelper.scannerMethods(services.get(Constants.PROVIDER));
        //对需要订阅的服务初始化本地容器
        List<ServiceConfig> consumerServices = services.get(Constants.CONSUMER);
        if (CollectionUtils.isNotEmpty(consumerServices))
        {
            consumersMap = Maps.newConcurrentMap();
            //转化获取所有消费接口的注册路径
            consumerPaths = consumerServices.stream().map(consumer -> {
                consumersMap.put(consumer.getInterfaceName(), Lists.newArrayList());
                String path = "/" + consumer.getInterfaceName() + "/" + Constants.PROVIDER;
                return path;
            }).collect(Collectors.toList());
        }
        return services;
    }
    
    @Override
    public ConcurrentMap<String, List<String>> register(String appName, String appAddress)
    {
        //TODO 这里其实应该做spring的注册
        return consumersMap;
    }

    @Override
    public Map<String, List<ServiceConfig>> getServiceCaches()
    {
        return services;
    }

    @Override
    public ConcurrentMap<String, List<String>> getConsumerLists()
    {
        return consumersMap;
    }
    
    /**
     * 将监听注册中心的结果缓存到本地
     *
     * @param type 事件发生类别
     * @param path
     * @param dataStr
     */
    public static void cache(TreeCacheEvent.Type type, String path, String dataStr)
    {
        //拆解当前的路径
        List<String> pathNodes = Splitter.on('/').trimResults().omitEmptyStrings().splitToList(path);
        if (!pathNodes.contains(Constants.PROVIDER))
        {
            return;
        }
        //接口名
        String interfaceName = pathNodes.get(0);
        //服务提供方ip
        String serverAddress = pathNodes.get(pathNodes.size() - 1);
        List<String> existsServers = ListUtils.defaultIfNull(consumersMap.get(interfaceName), Lists.newArrayList());
        //服务列表不为空并且是删除操作
        if (type.equals(NODE_REMOVED) && CollectionUtils.isNotEmpty(existsServers))
        {
            existsServers.remove(serverAddress);
            System.out.println(consumersMap);
        }
        else
        {
            if (StringUtils.isBlank(dataStr))
            {
                return;
            }
            if (!existsServers.contains(serverAddress))
            {
                existsServers.add(serverAddress);
            }
            List<String> pathInfos = Splitter.on('?').trimResults().omitEmptyStrings().splitToList(dataStr);
            Predicate<ServiceConfig> isInServer = (config) -> config.toUrlParam().equals(pathInfos.get(1));
            synchronized (services)
            {
                List<ServiceConfig> serviceConfigList =
                    ListUtils.defaultIfNull(services.get(path), Lists.newArrayList());
                if (serviceConfigList.stream().filter(isInServer).collect(Collectors.toList()).isEmpty())
                {
                    serviceConfigList.add(RegisterHelper.explainInfo(pathInfos.get(1)));
                    services.put(path, serviceConfigList);
                }
            }
        }
    }
}
