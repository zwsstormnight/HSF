package cn.nj.storm.shsf.core.entity;

/**
 * <服务配置对象>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ServiceConfig
{
    
    private String serviceType;
    
    private String name;
    
    private String interfaceName;
    
    private Class<?> interfaceClass;
    
    private Class<?> implementClass;
    
    private int retries;
    
    private int timeout;
    
    public String getServiceType()
    {
        return serviceType;
    }
    
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getInterfaceName()
    {
        return interfaceName;
    }
    
    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }
    
    public Class<?> getInterfaceClass()
    {
        return interfaceClass;
    }
    
    public void setInterfaceClass(Class<?> interfaceClass)
    {
        this.interfaceClass = interfaceClass;
    }
    
    public Class<?> getImplementClass()
    {
        return implementClass;
    }
    
    public void setImplementClass(Class<?> implementClass)
    {
        this.implementClass = implementClass;
    }
    
    public int getRetries()
    {
        return retries;
    }
    
    public void setRetries(int retries)
    {
        this.retries = retries;
    }
    
    public int getTimeout()
    {
        return timeout;
    }
    
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
    
    @Override
    public String toString()
    {
        return "ServiceConfig{" + "serviceType='" + serviceType + '\'' + ", name='" + name + '\'' + ", interfaceName='"
            + interfaceName + '\'' + ", interfaceClass=" + interfaceClass + ", implementClass=" + implementClass
            + ", retries=" + retries + ", timeout=" + timeout + '}';
    }
    
    public String toUrlParam()
    {
        //interface=&retries=&timeout=&type=&methods=
        return "interface=" + interfaceName + "&retries=" + retries + "&timeout=" + timeout + "&type=" + serviceType;
    }
}
