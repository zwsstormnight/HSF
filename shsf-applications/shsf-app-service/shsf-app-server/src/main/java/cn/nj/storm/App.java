package cn.nj.storm;

import cn.nj.storm.shsf.core.annotation.AutoShsfConfiguration;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoShsfConfiguration(name = "rpc-server")
public class App implements LoggerInterface
{
    public static void main(String[] args)
        throws Exception
    {
        //spring日志桥接
        SLF4JBridgeHandler.install();
        //启动Spring Boot项目的入口
        runLogger.info("Rpc-server 程序启动");
        SpringApplication.run(App.class, args);
        runLogger.info("Rpc-server 程序已启动");
        synchronized (App.class)
        {
            while (true)
            {
                System.out.println("........");
                App.class.wait();
            }
        }
    }
}
