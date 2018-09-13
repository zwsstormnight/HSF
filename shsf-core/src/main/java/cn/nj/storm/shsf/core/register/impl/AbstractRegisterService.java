package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.helper.RegisterHelper;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * <服务注册骨架类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class AbstractRegisterService implements RegisterService, LoggerInterface {

    protected String namespace;

    protected String appAddress;

    /**
     * 本地服务存储位置
     */
    protected static Map<String, Map<String, List<String>>> regMap;

    @Override
    public RegisterService scanner(String packageName) {
        Set<ServiceConfig> services = RegisterHelper.scannerInterfaces(packageName);
        if (CollectionUtils.isEmpty(services)) {
            return this;
        }
        Map<String, Set<MethodConfig>> serviceMethods = RegisterHelper.scannerMethods(services);
        regMap = Maps.newHashMap();
        for (ServiceConfig service : services) {
            //接口全名
            String serviceName = service.getInterfaceName();
            //拼接URL shsf://127.0.0.1:62338?interface=&retries=&timeout=&type=&methods=
            String params = service.toUrlParam() + "&methods=";
            Map<String, List<String>> typeMap = Maps.newHashMap();
            List<String> list = new ArrayList<>();
            for (MethodConfig methodConfig : serviceMethods.get(service.getName())) {
                params += (methodConfig.getName() + ",");
            }
            list.add(params);
            typeMap.put(service.getServiceType(), list);
            regMap.put(serviceName, typeMap);
        }
        return this;
    }

    @Override
    public String register(String appName, String appAddress) {
        return regMap.toString();
    }
}
