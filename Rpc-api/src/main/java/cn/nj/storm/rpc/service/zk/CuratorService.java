package cn.nj.storm.rpc.service.zk;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface CuratorService
{
    void controll();

    List<String> znodeAll();

    List<String> znodesByParent(String parent);
}
