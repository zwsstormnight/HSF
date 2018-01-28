package cn.nj.storm.rpc.service;

import cn.nj.storm.rpc.annotations.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RpcService(HappyService.class)
public class HappyServiceImpl implements HappyService
{
    public static Logger logger = LoggerFactory.getLogger("run");
    
    @Override
    public String makeHappy(String name)
    {
        logger.info("make happy name:" + name);
        return this.toString() + ":" + name;
    }
    
    @Override
    public void happyName()
    {
        logger.info("happy name");
    }
}
