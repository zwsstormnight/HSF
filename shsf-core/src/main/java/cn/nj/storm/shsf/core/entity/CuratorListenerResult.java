package cn.nj.storm.shsf.core.entity;

import lombok.Data;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * <Curator用于节点监听的结果>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class CuratorListenerResult
{
    public CuratorListenerResult()
    {
        
    }
    
    public CuratorListenerResult(TreeCacheEvent.Type type)
    {
        this.type = type;
    }
    
    public CuratorListenerResult(TreeCacheEvent.Type type, String path, String data)
    {
        this.type = type;
        this.path = path;
        this.data = data;
    }
    
    TreeCacheEvent.Type type;
    
    String path;
    
    String data;
}
