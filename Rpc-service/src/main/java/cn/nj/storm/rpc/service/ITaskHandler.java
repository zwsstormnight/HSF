package cn.nj.storm.rpc.service;

import cn.nj.storm.rpc.entity.TaskHandlerResp;

import java.util.List;

/**
 * <任务处理通用接口>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ITaskHandler
{
    /**
     * 执行无回调结果的线程执行过程
     */
    void runnableHandle();

    /**
     * 执行有回调结果的线程执行过程
     * @return TaskHandlerResp 任务执行回调参数
     */
    List<TaskHandlerResp> callableHandle();
}
