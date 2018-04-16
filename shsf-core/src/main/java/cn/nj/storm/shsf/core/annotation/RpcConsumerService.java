package cn.nj.storm.shsf.core.annotation;

import cn.nj.storm.shsf.core.utill.DefConstants;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Component
public @interface RpcConsumerService {

    /**
     * 接口实例类别
     * @return
     */
    Class<?> value() default Object.class;

    /**
     * 注册接口名称
     * @return
     */
    String name() default "";

    /**
     * 请求重试次数 默认3次
     * @return
     */
    int retries() default DefConstants.RETRY_TIMES;

    /**
     * 连接超时时间 默认是3000毫秒
     * @return
     */
    int timeout() default DefConstants.CONNECTION_TIMEOUT;
}
