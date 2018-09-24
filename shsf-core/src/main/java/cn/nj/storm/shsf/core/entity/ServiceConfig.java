package cn.nj.storm.shsf.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
public class ServiceConfig implements Serializable
{
    @JSONField(name = "type")
    private String serviceType;

    @JSONField(name = "name")
    private String name;
    
    @JSONField(name = "interface")
    private String interfaceName;

    @JSONField(serialize = false, deserialize = false)
    private Class<?> interfaceClass;

    @JSONField(serialize = false, deserialize = false)
    private Class<?> implementClass;
    
    @JSONField(name = "retries")
    private int retries;
    
    @JSONField(name = "timeout")
    private int timeout;
    
    @JSONField(serialize = false, deserialize = false)
    private Set<MethodConfig> methodConfigs;
    
    @JSONField(name = "methods")
    private List<String> methodNames;
    
    public void setMethodConfigs(Set<MethodConfig> methodConfigs)
    {
        this.methodConfigs = methodConfigs;
        if (CollectionUtils.isEmpty(this.methodConfigs))
        {
            return;
        }
        if (methodNames == null)
        {
            methodNames = Lists.newArrayList();
        }
        methodNames.addAll(Lists.transform(methodConfigs.stream().collect(Collectors.toList()),
            methodConfig -> methodConfig.getName()));
    }
    
    /**
     * 将当前类的字段转变为url参数的格式
     * @return
     */
    public String toUrlParam()
    {
        return "interface=" + interfaceName + "&retries=" + retries + "&timeout=" + timeout + "&type=" + serviceType
            + "&methods=" + this.methodNames.toString();
    }
}
