package cn.nj.storm.shsf.app.server.taskAsyncDemo.business.impl;

import cn.nj.storm.shsf.app.server.taskAsyncDemo.business.AliBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service("aliBusinessService")
public class AliBusinessServiceImpl implements AliBusinessService
{
    public static Logger logger = LoggerFactory.getLogger("run");
    
    @Override
    public void handle(String msg)
    {
        Thread currentThread = Thread.currentThread();
        String threadInfo = currentThread.getName() + "_" + currentThread.getId();
        logger.info("{}|{}|{}", "AliBusinessService", threadInfo, msg);
    }
}
