package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.entity.MethodConfig;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.utill.AnnotationUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LocalRegisterService extends AbstractRegisterService {

    private static final LocalRegisterService INSTANCE = new LocalRegisterService();

    private String namespace;

    private String appAddress;

    public static LocalRegisterService getInstance(String registerRoot) {
        INSTANCE.namespace = registerRoot;
        return INSTANCE;
    }

    private LocalRegisterService() {

    }

//    @Autowired
//    private ApplicationContext applicationContext;

    @Override
    public String register(String packageName) {
        Set<ServiceConfig> services = AnnotationUtils.getServices(packageName, RpcProviderService.class);
        Map<String, Set<MethodConfig>> serviceMethods = this.scannerMethods(services);


        return null;
    }

    private Map<String, Set<MethodConfig>> scannerMethods(Set<ServiceConfig> services) {
        if (CollectionUtils.isEmpty(services)) {
            return null;
        }
        Map<String, Set<MethodConfig>> methodMap = new HashMap<>();
        for (ServiceConfig serviceConfig : services) {
            Set<MethodConfig> methodConfigs = AnnotationUtils.getMethods(serviceConfig);
            methodMap.put(serviceConfig.getName(), methodConfigs);
        }
        return methodMap;
    }
}
