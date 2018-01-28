package cn.nj.storm.rpc.conf;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
public class CuratorClientConfig
{
    public static Logger logger = LoggerFactory.getLogger("run");
    
    @Value("${zk.serverLists}")
    private String zkList;
    
    @Value("${zk.baseSleepTimeMilliseconds}")
    private Integer baseSleepTimeMs;
    
    @Value("${zk.maxRetries}")
    private Integer maxRetries;
    
    @Value("${zk.maxSessionTimeoutMilliseconds}")
    private Integer maxSessionTimeoutMs;
    
    @Value("${zk.maxConnectTimeoutMilliseconds}")
    private Integer maxConnectTimeoutMs;
    
    /**
     * 连接状态
     */
    private String connectionState;
    
    /**
     * zookeeper客户端控制台
     */
    private CuratorFramework client;
    
    @Bean
    public RetryPolicy buildRetryPolicy()
    {
        return new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
    }
    
    @Bean
    public CuratorFramework curatorClient()
    {
        client =
            CuratorFrameworkFactory.newClient(zkList, maxSessionTimeoutMs, maxConnectTimeoutMs, buildRetryPolicy());
        client.start();
        System.out.println("zk client start successfully!");
        connectionState = "CONNECTED";
        addListener();
        return client;
    }
    
    /**
     * 连接状态监听
     */
    public void addListener()
    {
        client.getConnectionStateListenable().addListener(new ConnectionStateListener()
        {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState)
            {
                if (newState.equals(ConnectionState.CONNECTED))
                {
                    logger.info("连接");
                    connectionState = "CONNECTED";
                }
                if (newState.equals(ConnectionState.RECONNECTED))
                {
                    logger.info("重新连接");
                    connectionState = "RECONNECTED";
                    curatorClient();
                }
                if (newState.equals(ConnectionState.LOST))
                {
                    logger.info("丢失");
                    connectionState = "LOST";
                }
                if (newState.equals(ConnectionState.SUSPENDED))
                {
                    logger.info("暂停");
                    connectionState = "SUSPENDED";
                }
                if (newState.equals(ConnectionState.READ_ONLY))
                {
                    logger.info("只读");
                    connectionState = "READ_ONLY";
                }
            }
        });
    }
}
