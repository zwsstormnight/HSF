package cn.nj.storm.shsf.core.annotation;

import cn.nj.storm.shsf.core.conf.register.ShsfAutoConfig;
import cn.nj.storm.shsf.core.utill.DefConstants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ShsfAutoConfig.class})
public @interface AutoShsfConfiguration
{
    /**
     * <是否装配的开关>
     * @return
     */
    boolean autoRegister() default true;

    /**
     * <默认根结点名称>
     * @return
     */
    String name() default DefConstants.SHSF_NAME;

    /**
     * <注册中心类别>
     *  LOCAL：本地不注册
     *  DB
     *  REDIS
     *  ZOOKEEPER
     *  EUREKA
     *  NON: 无中心化注册
     */
    String centre() default DefConstants.LOCAL;

}
