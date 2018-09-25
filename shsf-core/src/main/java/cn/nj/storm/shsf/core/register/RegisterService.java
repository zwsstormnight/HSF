package cn.nj.storm.shsf.core.register;

import cn.nj.storm.shsf.core.entity.ServiceConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * <服务注册接口>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface RegisterService {

    /**
     * 扫描 当前包名称路径下的所有符合的接口
     *
     * @param packageName
     * @return
     */
    Map<String, List<ServiceConfig>> scanner(String packageName);

    /**
     * 注册 当前已经符合接口到注册中心
     *
     * @param appName
     * @param appAddress
     * @return
     */
    ConcurrentMap<String, List<String>> register(String appName, String appAddress);
}
