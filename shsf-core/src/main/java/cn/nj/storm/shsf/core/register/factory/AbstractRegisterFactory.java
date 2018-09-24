package cn.nj.storm.shsf.core.register.factory;

import cn.nj.storm.shsf.core.register.RegisterService;
import cn.nj.storm.shsf.core.register.IRegisterFactory;
import cn.nj.storm.shsf.core.utils.LoggerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
@Service
public class AbstractRegisterFactory implements IRegisterFactory, LoggerInterface {

    /**
     * 服务名称
     */
    protected String appName;

    /**
     * 当前服务的ip
     */
    protected InetAddress localAddress;

    /**
     * 扫描的包路径
     */
    protected Package scannerPackage;

    /**
     * 注册服务
     */
    public RegisterService registerService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public RegisterService create(Package scannerPackage, String appName, String registerCentre) {
        this.scannerPackage = scannerPackage;
        this.appName = appName;
        this.localAddress = getLocalInetAddress();
        registerService = (RegisterService) applicationContext.getBean(registerCentre);
        registerService.scanner(scannerPackage.getName());
        registerService.register(appName, this.localAddress.getHostAddress());
        return registerService;
    }

    public static InetAddress getLocalInetAddress() {
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
}