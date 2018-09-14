package cn.nj.storm.shsf.core.conf.zookeeper;

import cn.nj.storm.shsf.core.register.impl.SimpleRegisterService;
import cn.nj.storm.shsf.core.utill.Constants;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
@ConditionalOnProperty(prefix = "shsf.zookeeper", value = {"enabled"})
@EnableConfigurationProperties(value = {ZookeeperProperties.class})
public class CuratorClientConfig implements LoggerInterface {

    /**
     * 连接状态
     */
    private String connectionState;

    private CuratorFramework client;

    @Bean
    public RetryPolicy buildRetryPolicy(ZookeeperProperties properties) {
        return new ExponentialBackoffRetry(properties.getBaseSleepTimeMilliseconds(), properties.getMaxRetries(), properties.getMaxSleepTimeMilliseconds());
    }

    @Bean
    public CuratorFramework curatorClient(RetryPolicy retryPolicy, ZookeeperProperties properties) {
        client = CuratorFrameworkFactory.builder().
                connectString(properties.getServerLists()).
                sessionTimeoutMs(properties.getMaxSessionTimeoutMilliseconds()).
                connectionTimeoutMs(properties.getMaxConnectTimeoutMilliseconds()).
                retryPolicy(retryPolicy).
                namespace(Constants.SHSF_NAME).
                build();
        client.start();
        System.out.println("zk client start successfully!");
//        connectionState = "CONNECTED";
//        addListener(retryPolicy, properties);
        setTreeCacheListenter(client);
        return client;
    }

    /**
     * 连接状态监听
     */
    public void addListener(RetryPolicy retryPolicy, ZookeeperProperties properties) {
        client.getConnectionStateListenable().addListener((client, newState) -> {
            if (newState.equals(ConnectionState.CONNECTED)) {
                runLogger.info("连接");
                connectionState = "CONNECTED";
            }
            if (newState.equals(ConnectionState.RECONNECTED)) {
                runLogger.info("重新连接");
                connectionState = "RECONNECTED";
                curatorClient(retryPolicy, properties);
            }
            if (newState.equals(ConnectionState.LOST)) {
                runLogger.info("丢失");
                connectionState = "LOST";
            }
            if (newState.equals(ConnectionState.SUSPENDED)) {
                runLogger.info("暂停");
                connectionState = "SUSPENDED";
            }
            if (newState.equals(ConnectionState.READ_ONLY)) {
                runLogger.info("只读");
                connectionState = "READ_ONLY";
            }
        });
    }

    /**
     * 订阅默认空间的目录
     * @param client
     */
    private void setTreeCacheListenter(CuratorFramework client) {
        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, "/");
        //设置监听器和处理过程
        treeCache.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data != null) {
                String dataStr = data.getData() != null ? new String(data.getData()) : "";
                System.out.println(event.getType()+ ": " + data.getPath() + "  数据:" + dataStr);
                SimpleRegisterService.cache(event.getType(), data.getPath(), dataStr);
            } else {
                System.out.println("data is null : " + event.getType());
            }
        });
        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
