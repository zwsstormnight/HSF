package cn.nj.storm.shsf.rpc.proxy;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/27]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface RPCCallback
{
    void success(Object result);

    void fail(Exception e);
}
