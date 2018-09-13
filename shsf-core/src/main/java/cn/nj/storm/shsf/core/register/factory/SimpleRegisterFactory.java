package cn.nj.storm.shsf.core.register.factory;

import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.impl.ZkRegisterService;
import cn.nj.storm.shsf.core.utill.Constants;
import cn.nj.storm.shsf.core.utill.LoggerInterface;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SimpleRegisterFactory extends AbstractRegisterFactory implements LoggerInterface {

    public SimpleRegisterFactory() {

    }

    public SimpleRegisterFactory(Package scannerPackage, String appName, String registerCentre) {
        this.scannerPackage = scannerPackage;
        this.appName = appName;
        this.localAddress = getLocalInetAddress();
        switch (registerCentre) {
            case Constants.ZOOKEEPER:
                new ZkRegisterFactory();
                break;
            case Constants.EUREKA:
                new EurekaRegisterFactory();
                break;
        }
    }

    @Override
    public RegisterService create(Package scannerPackage, String appName, String registerCentre) {
        return null;
    }
}
