package cn.nj.storm.shsf.core.conf.register;

import cn.nj.storm.shsf.core.annotation.AutoShsfConfiguration;
import cn.nj.storm.shsf.core.register.IRegisterFactory;
import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.factory.AbstractRegisterFactory;
import cn.nj.storm.shsf.core.register.impl.AbstractRegisterService;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import cn.nj.storm.shsf.rpc.RpcServerFactory;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * <SHSF自动装配>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ShsfAutoConfig implements LoggerInterface {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ShsfProperties shsfProperties;

    @PostConstruct
    public void init() {
        Map<String, Object> appBeanMap = applicationContext.getBeansWithAnnotation(AutoShsfConfiguration.class);
        if (MapUtils.isNotEmpty(appBeanMap)) {
            Object appBean = appBeanMap.values().iterator().next();
            Class startClass = appBean.getClass();
            //获取注解对象
            AutoShsfConfiguration conf = AnnotationUtils.findAnnotation(startClass, AutoShsfConfiguration.class);
            //当注册注解不为空 并且 注解为可注册配置
            if (conf != null && conf.autoRegister()) {
                /**
                 * 这里需要尝试使用工厂的机制创建：registerFactory.create(startClass.getPackage(), conf.name(), conf.centre()) 这样也行;
                 */
                RegisterService registerService = (RegisterService) applicationContext.getBean(conf.centre());
                //扫描
                registerService.scanner(startClass.getPackage().getName());
                //注册到注册中心
                registerService.register(conf.name(), AbstractRegisterFactory.getLocalInetAddress().getHostAddress());
                //启动服务模式：netty/mina/rmi
                RpcServerFactory factory = new RpcServerFactory();
                factory.startServer(Integer.parseInt(shsfProperties.getPort()));
            }
        }
    }
}
