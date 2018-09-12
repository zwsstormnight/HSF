package cn.nj.storm.shsf.core.register.impl;

/**
 * <eureka注册业务类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EurekaRegisterService extends AbstractRegisterService {

    private static class SingletonHolder {
        public final static EurekaRegisterService INSTANCE = new EurekaRegisterService();
    }

    private EurekaRegisterService() {
    }

    public static EurekaRegisterService getInstance(String appName)
    {
        return SingletonHolder.INSTANCE;
    }
}
