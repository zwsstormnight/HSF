package cn.nj.storm.shsf.app.server.taskAsyncDemo.service.impl;

import cn.nj.storm.shsf.app.server.taskAsyncDemo.api.dto.TaskHandlerResp;
import cn.nj.storm.shsf.app.server.taskAsyncDemo.service.ITaskHandler;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class AbsTaskHandler implements ITaskHandler {

    @Override
    public void runnableHandle()
    {

    }

    @Override
    public List<TaskHandlerResp> callableHandle()
    {
        return null;
    }
}
