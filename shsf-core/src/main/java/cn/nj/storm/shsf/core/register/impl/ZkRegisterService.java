package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.conf.register.ShsfProperties;
import cn.nj.storm.shsf.core.entity.CuratorListenerResult;
import cn.nj.storm.shsf.core.entity.ServiceConfig;
import cn.nj.storm.shsf.core.utils.Constants;
import cn.nj.storm.shsf.core.utils.CuratorUtils;
import cn.nj.storm.shsf.core.utils.LoggerInterface;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String register(String appName, String appAddress)
    {
        CuratorFramework curatorFramework = applicationContext.getBean(CuratorFramework.class);
        //服务提供者注册到注册中心
        services.get(Constants.PROVIDER).forEach(serviceValue -> {
            String rolePath = "/" + serviceValue.getInterfaceName() + "/" + serviceValue.getServiceType();
            try
            {
                if (curatorFramework.checkExists().forPath(rolePath) == null)
                {
                    curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(rolePath, null);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            //拼接URL shsf://127.0.0.1:62338?interface=&retries=&timeout=&type=&methods=
            String serviceDetail = serviceValue.toUrlParam();
            serviceDetail = shsfProperties.getProtocal() + "://"
                + ObjectUtils.defaultIfNull(shsfProperties.getAddress(), appAddress) + ":" + shsfProperties.getPort()
                + "?" + serviceDetail;
            String detailPath = rolePath + "/" + ObjectUtils.defaultIfNull(shsfProperties.getAddress(), appAddress)
                + ":" + shsfProperties.getPort();
            try
            {
                curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(detailPath, serviceDetail.getBytes());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        //服务消费者监听注册中心
        listenConsumerServices(curatorFramework);
        //TODO        SimpleRegisterService.cache(result.getType(), result.getPath(), result.getData());
        //TODO  消费者构建动态代理
        return null;
    }
    
    private static List<String> getTreeNodes(CuratorFramework curatorFramework, String path)
    {
        List<String> nodes = null;
        try
        {
            final String nodePath = StringUtils.defaultIfBlank(path, "/");
            //先判断节点是否存在
            if (curatorFramework.checkExists().forPath(nodePath) != null)
            {
                nodes = curatorFramework.getChildren().forPath(nodePath);
            }
            nodes = nodes.stream()
                .map(childNode -> nodePath.endsWith("/") ? nodePath + childNode : nodePath + "/" + childNode)
                .collect(Collectors.toList());
            log.info("{}|{}|{}", "path get node", path, nodes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return nodes;
    }
    
    private static List<String> getTreeNodes(CuratorFramework curatorFramework, List<String> paths)
    {
        System.out.println(paths);
        List<String> nodes = Lists.newArrayList();
        List<String> temp = Lists.newArrayList();
        for (String path : paths)
        {
            List<String> list = getTreeNodes(curatorFramework, path);
            if (CollectionUtils.isEmpty(list))
            {
                temp.add(path);
                continue;
            }
            nodes.addAll(list);
        }
        if (CollectionUtils.isNotEmpty(nodes))
        {
            nodes.addAll(temp);
            return getTreeNodes(curatorFramework, nodes);
        }
        return paths;
    }
    
    private static Map<String, Object> getTreeNodes(CuratorFramework curatorFramework, Map<String, Object> nodes)
    {
        return nodes;
    }
    
    public static void getServicesInNamespace(CuratorFramework curatorFramework)
    {
        //全量获取当前命名空间下的各个节点的信息
        List<String> nodes = getTreeNodes(curatorFramework, getTreeNodes(curatorFramework, ""));
        //如果下级节点不存在则查询节点的值
        if (CollectionUtils.isNotEmpty(nodes))
        {
            long count = nodes.stream().map(node -> {
                try
                {
                    String dataStr = new String(curatorFramework.getData().forPath(node));
                    cache(TreeCacheEvent.Type.NODE_ADDED, node, dataStr);
                    return 1;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return 0;
            }).count();
            System.out.println(count);
        }
    }
    
    private static List<CuratorListenerResult> listenConsumerServices(CuratorFramework curatorFramework)
    {
        if (CollectionUtils.isEmpty(consumerPaths))
        {
            return null;
        }
        return consumerPaths.stream()
            .map(path -> CuratorUtils.setTreeCacheListener(curatorFramework, path))
            .collect(Collectors.toList());
    }
}
