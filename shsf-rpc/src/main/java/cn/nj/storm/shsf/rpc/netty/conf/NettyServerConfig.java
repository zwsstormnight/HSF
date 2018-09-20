package cn.nj.storm.shsf.rpc.netty.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ConfigurationProperties(prefix = "shsf.netty")
public class NettyServerConfig {

    private Integer corePoolSize;

    private Integer queueCapacity;

    private Integer maxPoolSize;

    private Integer keepAliveTime;

}
