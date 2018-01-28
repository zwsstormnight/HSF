package cn.nj.storm.rpc.conf;

import cn.nj.storm.rpc.annotations.RpcService;
import org.apache.commons.collections4.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
@AutoConfigureAfter({CuratorClientConfig.class})
public class ServiceRegistry
{
    public static Logger logger = LoggerFactory.getLogger("run");
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Value("${service.name.space}")
    private String namespace;
    
    @Value("${app.serverLists}")
    private String appAddress;
    
    private Map<String, Object> handlerMap = new HashMap<>();
    
    private ServiceRegistry()
    {
        
    }
    
    @Bean
    public ServiceRegistry registry()
        throws Exception
    {
        CuratorFramework curatorFramework = applicationContext.getBean(CuratorFramework.class);
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap))
        {
            try
            {
                for (Object serviceBean : serviceBeanMap.values())
                {
                    String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                    //节点路径
                    String interFaceIdNode = "/" + interfaceName;
                    //无当前节点
                    if (curatorFramework.checkExists().forPath(interFaceIdNode) == null)
                    {
                        //创建的路径和值
                        curatorFramework.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                            .forPath(interFaceIdNode);
                    }
                    //节点路径
                    String ipNode = interFaceIdNode + "/" + appAddress;
                    if (curatorFramework.checkExists().forPath(ipNode) != null)
                    {
                        //有当前IP的接点，则删除后，重新建立
                        curatorFramework.delete().forPath(ipNode);
                    }
                    //创建的路径和值
                    curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(ipNode);
                }
            }
            catch (Exception e)
            {
                logger.error("create zookeeper node failure", e);
            }
        }
        return new ServiceRegistry();
    }
    
}
