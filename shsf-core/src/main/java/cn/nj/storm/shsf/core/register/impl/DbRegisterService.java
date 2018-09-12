package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.utill.LoggerInterface;

/**
 * <数据库注册实现>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DbRegisterService extends AbstractRegisterService implements LoggerInterface
{
    private static class SingletonHolder {
        public final static DbRegisterService INSTANCE = new DbRegisterService();
    }

    private DbRegisterService() {
    }

}
