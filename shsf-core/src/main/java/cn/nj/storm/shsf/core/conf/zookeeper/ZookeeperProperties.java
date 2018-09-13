package cn.nj.storm.shsf.core.conf.zookeeper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ConfigurationProperties(prefix = "shsf.zookeeper")
public class ZookeeperProperties {

    private String serverLists;

    /**
     * Is Zookeeper enabled
     */
    private boolean enabled = false;

    private Integer baseSleepTimeMilliseconds;

    private Integer maxRetries;

    private Integer maxSleepTimeMilliseconds;

    private Integer maxSessionTimeoutMilliseconds;

    private Integer maxConnectTimeoutMilliseconds;

    /**
     * Wait time to block on connection to Zookeeper
     */
    private Integer blockUntilConnectedWait = 10;

    /**
     * The unit of time related to blocking on connection to Zookeeper
     */
    private TimeUnit blockUntilConnectedUnit = TimeUnit.SECONDS;
}
