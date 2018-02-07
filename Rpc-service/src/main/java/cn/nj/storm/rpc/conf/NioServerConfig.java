package cn.nj.storm.rpc.conf;

import cn.nj.storm.rpc.service.nio.NioServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/7]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
public class NioServerConfig
{
    private static final int PORT = 8080;
    
    @Bean
    public Selector selector()
    {
        Selector selector = null;
        ServerSocketChannel channel = null;
        try
        {
            //创建一个Selector实例（使用静态工厂方法open()）
            selector = Selector.open();
            //打开一个ServerSocket通道
            channel = ServerSocketChannel.open();
            //channel绑定当前server的地址
            channel.socket().bind(new InetSocketAddress(PORT));
            //非阻塞
            channel.configureBlocking(false);
            /**
             * 注册（register）到想要监控的信道上（注意，这要通过channel的方法实现，而不是使用selector的方法）
             *
             * 通道触发了一个事件意思是该事件已经就绪。所以，某个channel成功连接到另一个服务器称为“连接就绪”。
             *      SelectionKey.OP_CONNECT
             * 一个server socket channel准备好接收新进入的连接称为“接收就绪”。
             *      SelectionKey.OP_ACCEPT
             * 一个有数据可读的通道可以说是“读就绪”。
             *      SelectionKey.OP_READ
             * 等待写数据的通道可以说是“写就绪”。
             *      SelectionKey.OP_WRITE
             *
             */
            channel.register(selector, SelectionKey.OP_ACCEPT);
            new NioServer(selector).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            try
            {
                if (selector != null)
                {
                    selector.close();
                }
                if (channel != null)
                {
                    channel.close();
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return selector;
    }
}
