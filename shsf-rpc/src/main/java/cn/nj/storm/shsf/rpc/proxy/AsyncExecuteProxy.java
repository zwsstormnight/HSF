package cn.nj.storm.shsf.rpc.proxy;

import cn.nj.storm.shsf.rpc.async.ShsfFutureResponse;

/**
 * <异步执行的代理接口>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface AsyncExecuteProxy
{
    ShsfFutureResponse call(String funcName, Object... args);
}