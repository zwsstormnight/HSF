package cn.nj.storm.shsf.rpc.protocal;

import lombok.Data;

@Data
public class ShsfResponse
{
    private String requestId;

    private String code;
    
    private String error;
    
    private Object result;
}
