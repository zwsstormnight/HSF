package cn.nj.storm.rpc.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TaskHandlerReq
{
    private Map<String, Object> extra;
    
    private String msg;
    
    private Object serviceImplement;
    
    private Class clazz;
    
    public Map<String, Object> getExtra()
    {
        return extra;
    }
    
    public void setExtra(Map<String, Object> extra)
    {
        this.extra = extra;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public Object getServiceImplement()
    {
        return serviceImplement;
    }
    
    public void setServiceImplement(Object serviceImplement)
    {
        this.serviceImplement = serviceImplement;
    }
    
    public Class getClazz()
    {
        return clazz;
    }
    
    public void setClazz(Class clazz)
    {
        this.clazz = clazz;
    }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
