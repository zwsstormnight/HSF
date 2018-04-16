package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.register.RegisterService;

/**
 * <服务注册骨架类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class AbstractRegisterService implements RegisterService
{
    public AbstractRegisterService()
    {
        
    }

    public AbstractRegisterService(String registerRoot)
    {
        this.registerRoot = registerRoot;
    }
    
    private String registerRoot;
    
    @Override
    public String register()
    {
        return null;
    }
}
