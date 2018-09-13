package cn.nj.storm.shsf.core.conf.zookeeper;

import cn.nj.storm.shsf.core.utill.Constants;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
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
                build();
        client.start();
        System.out.println("zk client start successfully!");
        connectionState = "CONNECTED";
        addListener(retryPolicy, properties);
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

    private static void setTreeCacheListenter(CuratorFramework client)
            throws Exception {
        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, "/test");
        //设置监听器和处理过程
        treeCache.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data != null) {
                switch (event.getType()) {
                    case NODE_ADDED:
                        System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    case NODE_REMOVED:
                        System.out
                                .println("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    case NODE_UPDATED:
                        System.out
                                .println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    default:
                        break;
                }
            } else {
                System.out.println("data is null : " + event.getType());
            }
        });
        //开始监听
        treeCache.start();
    }
}
