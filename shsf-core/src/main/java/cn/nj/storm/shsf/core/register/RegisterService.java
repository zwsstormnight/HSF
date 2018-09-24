package cn.nj.storm.shsf.core.register;

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
     * 扫描 当前包名称路径下的所有符合的
     *
     * @param packageName
     * @return
     */
    RegisterService scanner(String packageName);

    /**
     * 注册
     *
     * @param appName
     * @param appAddress
     * @return
     */
    String register(String appName, String appAddress);

}
