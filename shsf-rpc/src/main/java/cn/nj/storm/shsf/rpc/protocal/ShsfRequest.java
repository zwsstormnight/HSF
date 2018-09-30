package cn.nj.storm.shsf.rpc.protocal;

import lombok.Data;

/**
 * @author zwsst
 */
@Data
public class ShsfRequest
{
    /**
     * 因为要做拆包粘包 所以要根据requestId做
     */
    private String requestId;
    
    private String className;

    private String beanName;
    
    private String methodName;
    
    private Class<?>[] parameterTypes;
    
    private Object[] parameters;
}