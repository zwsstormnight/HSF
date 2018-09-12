package cn.nj.storm.shsf.core.conf.register;

import cn.nj.storm.shsf.core.annotation.AutoShsfConfiguration;
import cn.nj.storm.shsf.core.register.factory.AbstractRegisterFactory;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
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
                AbstractRegisterFactory.getInstance(startClass.getPackage(), conf.name()).create(conf.centre());
            }
        }
    }
}
