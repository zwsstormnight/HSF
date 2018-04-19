package cn.nj.storm.shsf.core.register;

import cn.nj.storm.shsf.core.register.impl.DbRegisterService;
import cn.nj.storm.shsf.core.register.impl.LocalRegisterService;
import cn.nj.storm.shsf.core.register.impl.ZkRegisterService;
import cn.nj.storm.shsf.core.utill.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    
    public static RegisterServiceFactory getInstance(Package scannerPackage, String name, String centre)
    {
        INSTANCE.appName = name;
        INSTANCE.registerCentre = centre;
        INSTANCE.scannerPackage = scannerPackage;
        try
        {
            INSTANCE.localhost = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
            INSTANCE.localhost = "127.0.0.1";
            e.printStackTrace();
        }
        return INSTANCE;
    }
    
    private String appName;
    
    private String registerCentre;
    
    private String localhost;
    
    private Package scannerPackage;
    
    public RegisterService create()
    {
        RegisterService registerService = null;
        if (registerCentre.equals(Constants.LOCAL))
        {
            registerService = LocalRegisterService.getInstance(appName);
        }
        else if (registerCentre.equals(Constants.ZK))
        {
            registerService = new ZkRegisterService(appName);
        }
        else if (registerCentre.equals(Constants.DB))
        {
            registerService = new DbRegisterService(appName);
        }
        else if (registerCentre.equals(Constants.REDIS))
        {
            
        }
        else if (registerCentre.equals(Constants.EUREKA))
        {
            
        }
        else if (registerCentre.equals(Constants.CLUSTER))
        {
            
        }
        registerService.register(scannerPackage.getName());
        return registerService;
    }
}
