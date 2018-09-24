package cn.nj.storm.shsf.core.utils;

import cn.nj.storm.shsf.core.entity.CuratorListenerResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.concurrent.atomic.AtomicReference;

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
        AtomicReference<CuratorListenerResult> result = null;
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
        AtomicReference<CuratorListenerResult> result = null;
        treeCache.getListenable().addListener((client, event) -> {
            ChildData data = event.getData();
            if (data != null)
            {
                String dataStr = data.getData() != null ? new String(data.getData()) : "";
                System.out.println(event.getType() + ": " + data.getPath() + "  数据:" + dataStr);
                result.set(new CuratorListenerResult(event.getType(), data.getPath(), dataStr));
            }
            else
            {
                log.warn("{}|{}", "data is null", event.getType());
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
}