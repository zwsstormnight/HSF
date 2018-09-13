package cn.nj.storm.shsf.core.annotation;

import cn.nj.storm.shsf.core.conf.register.ShsfAutoStarter;
import cn.nj.storm.shsf.core.utill.Constants;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/12]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ShsfAutoStarter.class})
public @interface ShsfApplication {
    /**
     * <是否装配的开关>
     *
     * @return
     */
    boolean autoRegister() default true;

    /**
     * <默认根结点名称>
     *
     * @return
     */
    String name() default Constants.SHSF_NAME;

    /**
     * <注册中心类别>
     * LOCAL：本地不注册
     * DB
     * REDIS
     * ZOOKEEPER
     * EUREKA
     * NON: 无中心化注册
     */
    String centre() default Constants.LOCAL;
}
