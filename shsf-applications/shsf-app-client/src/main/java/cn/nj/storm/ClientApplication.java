package cn.nj.storm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication
{
    private static final Logger logger = LoggerFactory.getLogger("interface");

    public static void main(String[] args)
            throws Exception
    {
        //spring日志桥接
        SLF4JBridgeHandler.install();
        //启动Spring Boot项目的入口
        logger.info("Rpc-client 程序启动");
        SpringApplication.run(ClientApplication.class, args);
        logger.info("Rpc-client 程序已启动");
        synchronized (ClientApplication.class)
        {
            while (true)
            {
                System.out.println("........");
                ClientApplication.class.wait();
            }
        }
    }
}
