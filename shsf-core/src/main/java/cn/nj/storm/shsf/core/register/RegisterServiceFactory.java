package cn.nj.storm.shsf.core.register;

import cn.nj.storm.shsf.core.register.impl.AbstractRegisterService;
import cn.nj.storm.shsf.core.register.impl.DbRegisterService;
import cn.nj.storm.shsf.core.register.impl.ZkRegisterService;
import cn.nj.storm.shsf.core.utill.DefConstants;

/**
 * <服务注册器工厂>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RegisterServiceFactory
{
    private static final RegisterServiceFactory INSTANCE = new RegisterServiceFactory();

    private RegisterServiceFactory()
    {
    }
    
    public static RegisterServiceFactory getInstance(String name, String centre)
    {
        INSTANCE.appName = name;
        INSTANCE.registerCentre = centre;
        return INSTANCE;
    }
    
    private String appName;
    
    private String registerCentre;
    
    public RegisterService create()
    {
        RegisterService registerService = null;
        if (registerCentre.equals(DefConstants.LOCAL))
        {
            
        }
        else if (registerCentre.equals(DefConstants.ZK))
        {
            registerService = new ZkRegisterService(appName);
        }
        else if (registerCentre.equals(DefConstants.DB))
        {
//            registerService = new DbRegisterService(appName);
        }
        else if (registerCentre.equals(DefConstants.REDIS))
        {

        }
        else if (registerCentre.equals(DefConstants.EUREKA))
        {

        }
        else if (registerCentre.equals(DefConstants.CLUSTER))
        {

        }
        registerService.register();
        return registerService;
    }
}