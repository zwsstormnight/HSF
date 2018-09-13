package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.helper.RegisterHelper;
import cn.nj.storm.shsf.core.utill.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <本地>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
//@Service(value = Constants.LOCAL)
public class SimpleRegisterService extends AbstractRegisterService
{

//    private static class SingletonHolder {
//        public final static SimpleRegisterService INSTANCE = new SimpleRegisterService();
//    }
//
//    private SimpleRegisterService() {
//    }
//
//    public static SimpleRegisterService getInstance(String appName)
//    {
//        SingletonHolder.INSTANCE.namespace = appName;
//        regMap = Maps.newHashMap();
//        return SingletonHolder.INSTANCE;
//    }
    
//    @Override
//    public String register(String packageName)
//    {
//        Set<ServiceConfig> services = RegisterHelper.scannerInterfaces(packageName);
//        if (CollectionUtils.isEmpty(services))
//        {
//            return null;
//        }
//        Map<String, Set<MethodConfig>> serviceMethods = RegisterHelper.scannerMethods(services);
//        Map<String, Map<String, List<String>>> map = new HashMap<>();
//        for (ServiceConfig service : services)
//        {
//            //接口全名
//            String serviceName = service.getInterfaceName();
//            //拼接URL shsf://127.0.0.1:62338?interface=&retries=&timeout=&type=&methods=
//            String params = service.toUrlParam() + "&methods=";
//            Map<String, List<String>> typeMap = new HashMap<>();
//            List<String> list = new ArrayList<>();
//            for (MethodConfig methodConfig : serviceMethods.get(service.getName()))
//            {
//                params += (methodConfig.getName() + ",");
//            }
//            list.add(params);
//            typeMap.put(service.getServiceType(), list);
//            map.put(serviceName, typeMap);
//        }
//        regMap.put(SingletonHolder.INSTANCE.namespace, map);
//        return regMap.toString();
//    }
}
