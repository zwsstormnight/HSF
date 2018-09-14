package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.utill.Constants;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type.NODE_REMOVED;

/**
 * <本地注册服务机制的维护>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
//@Service(value = Constants.LOCAL)
public class SimpleRegisterService extends AbstractRegisterService
{
    
    /**
     * 事件发生类别
     *
     * @param type
     * @param path
     * @param dataStr
     */
    public static void cache(TreeCacheEvent.Type type, String path, String dataStr)
    {
        if (StringUtils.isBlank(dataStr))
        {
            return;
        }
        //拆解当前的路径
        List<String> pathNodes = Splitter.on('/').trimResults().omitEmptyStrings().splitToList(path);
        if (!pathNodes.contains(Constants.PROVIDER))
        {
            return;
        }
        //接口名
        String interfaceName = pathNodes.get(0);
        //接口提供方ip
        String serverAddress = pathNodes.get(pathNodes.size() - 1);
        //服务提供者信息
        List<Map<String, Object>> existsInfos = ListUtils.emptyIfNull(totalServices.get(interfaceName));
        Predicate<Map<String, Object>> isInServer = (map) -> map.containsKey(serverAddress);
        //服务列表不为空并且是删除操作
        if (type.equals(NODE_REMOVED))
        {
            existsInfos.removeAll(existsInfos.stream().filter(isInServer).collect(Collectors.toList()));
        }
        else
        {
            List<String> pathInfos = Splitter.on('?').trimResults().omitEmptyStrings().splitToList(dataStr);
            System.out.println(pathInfos);
            Map<String, Object> m = Maps.newHashMap();
//            m.put(serverAddress, dataStr);
//            existsInfos.add(m);
        }
        //TODO 解析接口详细信息
        totalServices.put(interfaceName, existsInfos);
        System.out.println(totalServices);
    }
}
