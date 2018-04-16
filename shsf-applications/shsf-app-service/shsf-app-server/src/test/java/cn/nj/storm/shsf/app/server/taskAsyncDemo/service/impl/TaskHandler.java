package cn.nj.storm.shsf.app.server.taskAsyncDemo.service.impl;

import cn.nj.storm.shsf.app.server.taskAsyncDemo.api.dto.TaskHandlerReq;
import cn.nj.storm.shsf.app.server.taskAsyncDemo.api.dto.TaskHandlerResp;
import cn.nj.storm.shsf.app.server.taskAsyncDemo.service.ThreadHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/16]
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
