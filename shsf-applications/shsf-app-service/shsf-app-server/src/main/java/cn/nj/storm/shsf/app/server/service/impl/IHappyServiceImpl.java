package cn.nj.storm.shsf.app.server.service.impl;

import cn.nj.storm.shsf.app.api.service.IHappyService;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.utill.LoggerInterface;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RpcProviderService(IHappyService.class)
public class IHappyServiceImpl implements IHappyService,LoggerInterface {

    @Override
    public String makeHappy(String name)
    {
        interfaceLogger.info("make happy name:" + name);
        return this.toString() + ":" + name;
    }

    @Override
    public void happyName()
    {
        interfaceLogger.info("happy name");
    }
}
