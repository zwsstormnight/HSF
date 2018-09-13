package cn.nj.storm.shsf.core.register.factory;

import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.utill.LoggerInterface;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ZkRegisterFactory extends SimpleRegisterFactory implements LoggerInterface {

    public ZkRegisterFactory() {
    }

    @Override
    public RegisterService create(Package scannerPackage, String appName, String registerCentre) {
        return null;
    }
}
