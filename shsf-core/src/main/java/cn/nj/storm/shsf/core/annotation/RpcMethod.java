package cn.nj.storm.shsf.core.annotation;

import java.lang.annotation.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {

    /**
     * 方法类别
     * @return
     */
    Class<?> value() default Object.class;

    /**
     * 注册方法名称
     * @return
     */
    String name() default "";

}
