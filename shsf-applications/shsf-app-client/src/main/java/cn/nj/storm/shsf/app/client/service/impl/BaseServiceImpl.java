package cn.nj.storm.shsf.app.client.service.impl;

import cn.nj.storm.shsf.app.api.service.IHappyService;
import cn.nj.storm.shsf.app.client.service.BaseService;
import cn.nj.storm.shsf.core.annotation.RpcConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service(value = "baseService")
public class BaseServiceImpl implements BaseService {

    @RpcConsumerService(name = "iHappyService", interfaceClass = IHappyService.class)
    private IHappyService iHappyService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String toMakeHappy(String name){
        iHappyService = applicationContext.getBean(IHappyService.class);
        return iHappyService.makeHappy(name);
    }
}
