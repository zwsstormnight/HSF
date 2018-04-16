package cn.nj.storm.shsf.core.conf;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CuratorClientConfig implements LoggerInterface
{
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
                    runLogger.info("连接");
                    connectionState = "CONNECTED";
                }
                if (newState.equals(ConnectionState.RECONNECTED))
                {
                    runLogger.info("重新连接");
                    connectionState = "RECONNECTED";
                    curatorClient();
                }
                if (newState.equals(ConnectionState.LOST))
                {
                    runLogger.info("丢失");
                    connectionState = "LOST";
                }
                if (newState.equals(ConnectionState.SUSPENDED))
                {
                    runLogger.info("暂停");
                    connectionState = "SUSPENDED";
                }
                if (newState.equals(ConnectionState.READ_ONLY))
                {
                    runLogger.info("只读");
                    connectionState = "READ_ONLY";
                }
            }
        });
    }
    
    private static void setTreeCacheListenter(CuratorFramework client)
        throws Exception
    {
        ExecutorService pool = Executors.newCachedThreadPool();
        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, "/test");
        //设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener()
        {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event)
                throws Exception
            {
                ChildData data = event.getData();
                if (data != null)
                {
                    switch (event.getType())
                    {
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
                }
                else
                {
                    System.out.println("data is null : " + event.getType());
                }
            }
        });
        //开始监听
        treeCache.start();
    }
}
