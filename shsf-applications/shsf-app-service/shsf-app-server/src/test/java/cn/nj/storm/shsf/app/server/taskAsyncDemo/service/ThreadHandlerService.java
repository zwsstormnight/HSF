package cn.nj.storm.shsf.app.server.taskAsyncDemo.service;

/**
 * <线程业务处理接口>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ThreadHandlerService
{
    void handle(String msg);
}