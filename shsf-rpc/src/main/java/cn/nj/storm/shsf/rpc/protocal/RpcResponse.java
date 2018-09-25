package cn.nj.storm.shsf.rpc.protocal;

import lombok.Data;

@Data
public class RpcResponse
{
    private String requestId;
    
    private String error;
    
    private Object result;
    
    public boolean isError()
    {
        return error != null;
    }
}
