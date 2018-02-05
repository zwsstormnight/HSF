package cn.nj.storm.rpc.service.task.impl;

import cn.nj.storm.App;
import cn.nj.storm.rpc.entity.TaskHandlerReq;
import cn.nj.storm.rpc.entity.TaskParam;
import cn.nj.storm.rpc.service.business.AliBusinessService;
import cn.nj.storm.rpc.service.business.AmazonBusinessService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TaskHandlerTest
{
    @Autowired
    private TaskHandler taskHandler;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    public void runnableHandle()
    {
        TaskParam taskParam = new TaskParam();
        taskParam.setTaskId(0L);
        taskParam.setTransCode(UUID.randomUUID().toString());
        taskParam.setCreateTime(new Date());
        for (int i = 1; i <= 50; i++)
        {
            //重新赋值
            taskParam.setTaskId((long)i);
            taskParam.setTransCode(UUID.randomUUID().toString());
            TaskHandlerReq taskHandlerReq = new TaskHandlerReq();
            taskHandlerReq.setMsg(JSON.toJSONString(taskParam));
            //这个if只是为了demo演示
            if (i % 3 == 0)
            {
                taskHandlerReq.setServiceImplement(applicationContext.getBean(AliBusinessService.class));
                taskHandlerReq.setClazz(AliBusinessService.class);
            }
            else
            {
                taskHandlerReq.setServiceImplement(applicationContext.getBean(AmazonBusinessService.class));
                taskHandlerReq.setClazz(AmazonBusinessService.class);
            }
            taskHandler.add(taskHandlerReq);
        }
        taskHandler.runnableHandle();
    }
    
    @Test
    public void callableHandle()
    {
        
    }
    
}