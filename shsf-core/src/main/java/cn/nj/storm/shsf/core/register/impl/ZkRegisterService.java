package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.conf.register.ShsfProperties;
import cn.nj.storm.shsf.core.utils.Constants;
import cn.nj.storm.shsf.core.utils.CuratorUtils;
import cn.nj.storm.shsf.core.utils.LoggerInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * <zookeeper注册实现>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service(value = Constants.ZOOKEEPER)
@Slf4j
public class ZkRegisterService extends AbstractRegisterService implements LoggerInterface
{
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ShsfProperties shsfProperties;
    
    @Override
    public ConcurrentMap<String, List<String>> register(String appName, String appAddress)
    {
        CuratorFramework curatorFramework = applicationContext.getBean(CuratorFramework.class);
        if (curatorFramework == null || MapUtils.isEmpty(services))
        {
            log.error("{}|{}|{}", "registe to zookeeper fail..", curatorFramework, services);
            return null;
        }
        services.keySet().forEach(roleName -> {
            //服务提供者，消费者注册到注册中心
            services.get(roleName).forEach(serviceValue -> {
                String rolePath = "/" + serviceValue.getInterfaceName() + "/" + serviceValue.getServiceType();
                CuratorUtils.createPersistentNodeUnsafe(curatorFramework, rolePath, null);
                String serviceDetail = serviceValue.toJsonParam();
                serviceDetail = shsfProperties.getProtocal() + "://"
                    + ObjectUtils.defaultIfNull(shsfProperties.getAddress(), appAddress) + ":"
                    + shsfProperties.getPort() + "?" + serviceDetail;
                String detailPath = rolePath + "/" + ObjectUtils.defaultIfNull(shsfProperties.getAddress(), appAddress)
                    + ":" + shsfProperties.getPort();
                CuratorUtils.createEphemeralNodeUnsafe(curatorFramework, detailPath, serviceDetail.getBytes());
            });
        });
        //服务消费者监听注册中心
        listenConsumerServices(curatorFramework);
        return consumersMap;
    }
    
    /**
     * 监听当前消费服务的节点的变化
     * @param curatorFramework
     */
    private static void listenConsumerServices(CuratorFramework curatorFramework)
    {
        if (CollectionUtils.isEmpty(consumerPaths))
        {
            return;
        }
        for (String path : consumerPaths)
        {
            log.warn("{}|{}", "start listen current tree path :", path);
            TreeCache treeCache = new TreeCache(curatorFramework, path);
            treeCache.getListenable().addListener((client, event) -> {
                if (event.getType().equals(TreeCacheEvent.Type.NODE_ADDED)
                    || event.getType().equals(TreeCacheEvent.Type.NODE_UPDATED)
                    || event.getType().equals(TreeCacheEvent.Type.NODE_REMOVED))
                {
                    ChildData data = event.getData();
                    if (data != null)
                    {
                        String dataStr = data.getData() != null ? new String(data.getData()) : "";
                        log.warn("{}|type:{}|path:{}|data:{}",
                            "tree listener event result:",
                            event.getType(),
                            data.getPath(),
                            dataStr);
                        SimpleRegisterService.cache(event.getType(), data.getPath(), dataStr);
                    }
                    else
                    {
                        log.warn("{}|type:{}", "tree listener event result is null", event.getType());
                    }
                }
            });
            try
            {
                treeCache.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
