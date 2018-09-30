package cn.nj.storm.shsf.core.annotation;

import cn.nj.storm.shsf.core.utils.Constants;

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
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
public @interface RpcConsumerService {

    /**
     * 接口实例类别
     * @return
     */
    Class<?> value() default Object.class;

    /**
     * 接口类别
     * @return
     */
    Class<?> interfaceClass();

    /**
     * 接口版本
     * @return
     */
    String version() default "1.0.0";

    /**
     * 注册接口名称
     * @return
     */
    String name() default "";

    /**
     * 请求重试次数 默认3次
     * @return
     */
    int retries() default Constants.RETRY_TIMES;

    /**
     * 连接超时时间 默认是3000毫秒
     * @return
     */
    int timeout() default Constants.CONNECTION_TIMEOUT;

    /**
     * 服务接口类别
     */
    String SERVICE_TYPE = Constants.CONSUMER;
}
