package cn.nj.storm.shsf.core.register.factory;

import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.IRegisterFactory;
import cn.nj.storm.shsf.core.register.impl.ClusterRegisterService;
import cn.nj.storm.shsf.core.register.impl.EurekaRegisterService;
import cn.nj.storm.shsf.core.register.impl.SimpleRegisterService;
import cn.nj.storm.shsf.core.register.impl.ZkRegisterService;
import cn.nj.storm.shsf.core.utill.Constants;
import cn.nj.storm.shsf.core.utill.LoggerInterface;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * <注册服务的抽象工厂类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AbstractRegisterFactory implements IRegisterFactory, LoggerInterface {

    private static class SingletonHolder {
        public final static AbstractRegisterFactory INSTANCE = new AbstractRegisterFactory();
    }

    private AbstractRegisterFactory() {
    }

    public static AbstractRegisterFactory getInstance(Package scannerPackage, String appName) {
        AbstractRegisterFactory.SingletonHolder.INSTANCE.scannerPackage = scannerPackage;
        AbstractRegisterFactory.SingletonHolder.INSTANCE.appName = appName;
        AbstractRegisterFactory.SingletonHolder.INSTANCE.localAddress = getLocalInetAddress();
        return AbstractRegisterFactory.SingletonHolder.INSTANCE;
    }

    protected String appName;

    protected InetAddress localAddress;

    protected Package scannerPackage;

    public RegisterService registerService;

    @Override
    public RegisterService create(String registerCentre) {
        switch (registerCentre) {
            case Constants.ZOOKEEPER:
                registerService = ZkRegisterService.getInstance(appName);
                break;
            case Constants.EUREKA:
                registerService = EurekaRegisterService.getInstance(appName);
                break;
            case Constants.CLUSTER:
                registerService = ClusterRegisterService.getInstance(appName);
                break;
            default:
                registerService = SimpleRegisterService.getInstance(appName);
                break;
        }
        registerService.register(scannerPackage.getName());
        return registerService;
    }

    private static InetAddress getLocalInetAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (!netInterface.isUp() || netInterface.isLoopback() || netInterface.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    inetAddress = addresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        return inetAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            InetAddress inetAddress = getLocalInetAddress();
            System.out.println(inetAddress);
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}