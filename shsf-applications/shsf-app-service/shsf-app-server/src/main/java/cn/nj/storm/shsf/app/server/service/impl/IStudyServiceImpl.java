package cn.nj.storm.shsf.app.server.service.impl;

import cn.nj.storm.shsf.app.api.service.IStudyService;
import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.utill.LoggerInterface;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RpcProviderService(name = "iStudyService", value = IStudyServiceImpl.class, interfaceClass = IStudyService.class)
public class IStudyServiceImpl implements IStudyService, LoggerInterface {

    @Override
    public String studyHard(String name) {
        return null;
    }
}
