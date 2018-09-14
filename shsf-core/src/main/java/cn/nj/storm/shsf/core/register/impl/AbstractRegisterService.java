package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.helper.RegisterHelper;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    
    /**
     * 本地服务的存储位置
     */
    protected static Set<ServiceConfig> services;
    
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
        if (CollectionUtils.isEmpty(services))
        {
            return this;
        }
        serviceMethods = RegisterHelper.scannerMethods(services);
        regMap = Maps.newHashMap();
        for (ServiceConfig service : services)
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
}
