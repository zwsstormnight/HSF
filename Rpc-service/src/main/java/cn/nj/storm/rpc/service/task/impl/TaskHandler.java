package cn.nj.storm.rpc.service.task.impl;

import cn.nj.storm.rpc.entity.TaskHandlerReq;
import cn.nj.storm.rpc.entity.TaskHandlerResp;
import cn.nj.storm.rpc.service.ThreadHandlerService;
import cn.nj.storm.rpc.service.task.AbsTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <任务处理实现>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service("taskHandler")
public class TaskHandler extends AbsTaskHandler
{
    public static Logger logger = LoggerFactory.getLogger("run");
    
    public static ConcurrentLinkedQueue<TaskHandlerReq> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    /**
     * <添加到队列方法 将指定元素插入此队列的尾部>
     * @param taskHandlerReq 需要执行的任务对象
     * @return 成功返回true，否则抛出 IllegalStateException
     */
    public boolean add(TaskHandlerReq taskHandlerReq)
    {
        return (concurrentLinkedQueue.add(taskHandlerReq));
    }
    
    @Override
    public void runnableHandle()
    {
        while (concurrentLinkedQueue.size() > 0)
        {
            executorService.execute(new Runnable()
            {
                TaskHandlerReq taskHandlerReq = concurrentLinkedQueue.poll();
                @Override
                public void run()
                {
                    try
                    {
                        ThreadHandlerService threadHandlerService =
                            (ThreadHandlerService)taskHandlerReq.getServiceImplement();
                        threadHandlerService.handle(taskHandlerReq.getMsg());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    @Override
    public List<TaskHandlerResp> callableHandle()
    {
        List<Future<String>> resultList = new ArrayList<Future<String>>();
        return null;
    }
}
