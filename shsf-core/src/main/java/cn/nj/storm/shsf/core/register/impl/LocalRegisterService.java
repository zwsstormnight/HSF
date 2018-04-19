package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.helper.RegisterHelper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LocalRegisterService extends AbstractRegisterService
{
    
    private static final LocalRegisterService INSTANCE = new LocalRegisterService();
    
    private String namespace;
    
    private String appAddress;
    
    private static Map<String, Map<String, Map<String, List<String>>>> regMap;
    
    public static LocalRegisterService getInstance(String registerRoot)
    {
        INSTANCE.namespace = registerRoot;
        regMap = new HashMap<>();
        return INSTANCE;
    }
    
    private LocalRegisterService()
    {
        
    }
    
    @Override
    public String register(String packageName)
    {
        Set<ServiceConfig> services = RegisterHelper.scannerInterfaces(packageName);
        if (CollectionUtils.isEmpty(services))
        {
            return null;
        }
        Map<String, Set<MethodConfig>> serviceMethods = RegisterHelper.scannerMethods(services);
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        for (ServiceConfig service : services)
        {
            //接口全名
            String serviceName = service.getClazz().getName();
            //拼接URL
            //TODO shsf://127.0.0.1:62338?interface=&retries=&timeout=&type=&methods=
            Map<String, List<String>> typeMap = new HashMap<>();
            List<String> list = new ArrayList<>();
            for (MethodConfig methodConfig : serviceMethods.get(service.getName()))
            {
                methodConfig.getName();
                //TODO 拼接method信息
            }
            typeMap.put(service.getServiceType(), list);
            map.put(serviceName, typeMap);
        }
        regMap.put(INSTANCE.namespace, map);
        return null;
    }
}
