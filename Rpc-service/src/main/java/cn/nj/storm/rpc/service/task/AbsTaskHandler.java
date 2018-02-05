package cn.nj.storm.rpc.service.task;

import cn.nj.storm.rpc.entity.TaskHandlerResp;
import cn.nj.storm.rpc.service.ITaskHandler;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AbsTaskHandler implements ITaskHandler
{
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
