package cn.nj.storm.shsf.core.conf.register;

import cn.nj.storm.shsf.core.annotation.ShsfApplication;
import cn.nj.storm.shsf.core.register.IRegisterFactory;
import cn.nj.storm.shsf.core.register.factory.SimpleRegisterFactory;
import cn.nj.storm.shsf.core.utils.LoggerInterface;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/12]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ShsfAutoStarter implements LoggerInterface {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public IRegisterFactory start() {
        Map<String, Object> appBeanMap = applicationContext.getBeansWithAnnotation(ShsfApplication.class);
        if (MapUtils.isNotEmpty(appBeanMap)) {
            Object appBean = appBeanMap.values().iterator().next();
            Class startClass = appBean.getClass();
            //获取注解对象
            ShsfApplication conf = AnnotationUtils.findAnnotation(startClass, ShsfApplication.class);
            //当注册注解不为空 并且 注解为可注册配置
            if (conf != null && conf.autoRegister()) {
                return new SimpleRegisterFactory(startClass.getPackage(), conf.name(), conf.centre());
            }
        }
        return null;
    }
}
