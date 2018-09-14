package cn.nj.storm.shsf.core.entity;

import lombok.Data;

import java.util.Set;

/**
 * <服务配置对象>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class ServiceConfig
{
    
    private String serviceType;
    
    private String name;
    
    private String interfaceName;
    
    private Class<?> interfaceClass;
    
    private Class<?> implementClass;
    
    private int retries;
    
    private int timeout;

    private Set<MethodConfig>  methodConfigs;
    
    public String toUrlParam()
    {
        //interface=&retries=&timeout=&type=&methods=
        return "interface=" + interfaceName + "&retries=" + retries + "&timeout=" + timeout + "&type=" + serviceType;
    }
}
