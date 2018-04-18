package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.utill.AnnotationUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

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
    public String register() {
        System.out.println("start regist");
        AnnotationUtils.getServices("cn.nj.storm", RpcProviderService.class);
//        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcProviderService.class);
//        if (MapUtils.isNotEmpty(serviceBeanMap))
//        {
//
//        }
        return null;
    }
}
