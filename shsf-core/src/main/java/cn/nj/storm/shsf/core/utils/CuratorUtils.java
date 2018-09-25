package cn.nj.storm.shsf.core.utils;

import cn.nj.storm.shsf.core.entity.CuratorListenerResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <Curator工具类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/21]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class CuratorUtils
{
    
    private CuratorUtils()
    {
        
    }
    
    public static void createPersistentNodeUnsafe(CuratorFramework curatorFramework, String path, byte[] data)
    {
        try
        {
            if (curatorFramework.checkExists().forPath(path) == null)
            {
                curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(path, data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createEphemeralNodeUnsafe(CuratorFramework curatorFramework, String path, byte[] data)
    {
        try
        {
            if (curatorFramework.checkExists().forPath(path) == null)
            {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(path, data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 订阅默认空间的目录
     * @param client
     */
    public static CuratorListenerResult setNameSpaceListener(CuratorFramework client)
    {
        //设置整个树的cache
        TreeCache treeCache = new TreeCache(client, "/");
        return treeCacheListenerEvent(treeCache);
    }
    
    /**
     * 订阅指定目录的tree
     * @param client
     */
    public static CuratorListenerResult setTreeCacheListener(CuratorFramework client, String path)
    {
        log.warn("{}|{}", "start listen current tree path :", path);
        TreeCache treeCache = new TreeCache(client, path);
        return treeCacheListenerEvent(treeCache);
    }
    
    /**
     * 订阅指定节点的data
     * @param client
     */
    public static CuratorListenerResult setNodesCacheListener(CuratorFramework client, String path)
    {
        AtomicReference<CuratorListenerResult> result = new AtomicReference<>();
        //设置节点的cache
        NodeCache nodeCache = new NodeCache(client, path);
        //设置监听器返回结果
        nodeCache.getListenable().addListener(() -> {
            ChildData data = nodeCache.getCurrentData();
            if (data != null)
            {
                String dataStr = data.getData() != null ? new String(data.getData()) : "";
                result.set(new CuratorListenerResult(TreeCacheEvent.Type.NODE_ADDED, data.getPath(), dataStr));
            }
            else
            {
                result.set(new CuratorListenerResult(TreeCacheEvent.Type.NODE_REMOVED, data.getPath(), null));
            }
        });
        try
        {
            nodeCache.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.get();
    }
    
    /**
     * 设置监听器的返回结果
     * @param treeCache
     * @return
     */
    private static CuratorListenerResult treeCacheListenerEvent(TreeCache treeCache)
    {
        AtomicReference<CuratorListenerResult> result = new AtomicReference<>();
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
                    result.set(new CuratorListenerResult(event.getType(), data.getPath(), dataStr));
                }
                else
                {
                    log.warn("{}|type:{}", "tree listener event result is null", event.getType());
                    result.set(new CuratorListenerResult(event.getType()));
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
        return result.get();
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
}