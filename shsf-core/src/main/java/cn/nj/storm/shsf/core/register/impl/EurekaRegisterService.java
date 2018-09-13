package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.utill.Constants;
import org.springframework.stereotype.Service;

/**
 * <eureka注册业务类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service(value = Constants.EUREKA)
public class EurekaRegisterService extends AbstractRegisterService {

    @Override
    public String register(String appName, String appAddress) {
        return regMap.toString();
    }
}
