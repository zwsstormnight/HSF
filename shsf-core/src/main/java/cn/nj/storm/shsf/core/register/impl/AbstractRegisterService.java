package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.helper.RegisterHelper;
import cn.nj.storm.shsf.core.utill.Constants;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.*;
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
public abstract class AbstractRegisterService implements RegisterService, LoggerInterface
{
    
    protected String namespace;
    
    protected String appAddress;
    
    /**
     * 注册中心的全量接口信息
     */
    public static Map<String, List<Map<String, Object>>> totalServices = Maps.newHashMap();
    
    /**
     * 本地服务存储位置
     * key: interfaceName
     * ---cn.nj.storm.shsf.app.api.service.IHappyService
     * value: type -> interfaceInfo
     * ---provider -> interface=cn.nj.storm.shsf.app.api.service.IHappyService&retries=3&timeout=3000&type=provider&methods=[makeHappy, happyName]
     */
    protected static Map<String, Map<String, String>> regMap;
    
    protected static Map<String, Map<String, String>> consumerMap;
    
    /**
     * 本地服务的存储位置
     */
    protected static Map<String, List<ServiceConfig>> services;
    
    /**
     * 本地服务所涵方法的存储位置
     * key: interface是springbean的name
     * --- iHappyService
     */
    protected static Map<String, Set<MethodConfig>> serviceMethods;
    
    @Override
    public RegisterService scanner(String packageName)
    {
        services = RegisterHelper.scannerInterfaces(packageName);
        if (MapUtils.isEmpty(services))
        {
            return this;
        }
        serviceMethods = RegisterHelper.scannerMethods(services.get(Constants.PROVIDER));
        regMap = Maps.newHashMap();
        for (ServiceConfig service : services.get(Constants.PROVIDER))
        {
            //接口全名
            String serviceName = service.getInterfaceName();
            //拼接URL shsf://127.0.0.1:62338?interface=&retries=&timeout=&type=&methods=
            String params = service.toUrlParam() + "&methods="
                + Lists.transform(serviceMethods.get(service.getName()).stream().collect(Collectors.toList()),
                    methodConfig -> methodConfig.getName()).toString();
            Map<String, String> typeMap = Maps.newHashMap();
            typeMap.put(service.getServiceType(), params);
            regMap.put(serviceName, typeMap);
        }
        return this;
    }
    
    @Override
    public String register(String appName, String appAddress)
    {
        return regMap.toString();
    }
    
    /**
     * 事件发生类别
     *
     * @param type
     * @param path
     * @param dataStr
     */
    public static void cache(TreeCacheEvent.Type type, String path, String dataStr)
    {
        if (StringUtils.isBlank(dataStr))
        {
            return;
        }
        //拆解当前的路径
        List<String> pathNodes = Splitter.on('/').trimResults().omitEmptyStrings().splitToList(path);
        if (!pathNodes.contains(Constants.PROVIDER))
        {
            return;
        }
        //接口名
        String interfaceName = pathNodes.get(0);
        //接口提供方ip
        String serverAddress = pathNodes.get(pathNodes.size() - 1);
        //服务提供者信息
        List<Map<String, Object>> existsInfos =
            ListUtils.defaultIfNull(totalServices.get(interfaceName), Lists.newArrayList());
        Predicate<Map<String, Object>> isInServer = (map) -> map.containsKey(serverAddress);
        //服务列表不为空并且是删除操作
        if (type.equals(NODE_REMOVED))
        {
            existsInfos.removeAll(existsInfos.stream().filter(isInServer).collect(Collectors.toList()));
        }
        else
        {
            List<String> pathInfos = Splitter.on('?').trimResults().omitEmptyStrings().splitToList(dataStr);
            System.out.println(pathInfos);
            Map<String, Object> m = Maps.newHashMap();
            m.put(serverAddress, pathInfos);
            existsInfos.add(m);
        }
        //TODO 解析接口详细信息
        totalServices.put(interfaceName, existsInfos);
        System.out.println(totalServices);
    }
    
    /**
     * 解析注册节点的信息
     * @param dataStr
     */
    private Object explainInfo(String dataStr)
    {
        //TODO
        return null;
    }
}
