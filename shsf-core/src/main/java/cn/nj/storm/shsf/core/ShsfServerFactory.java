package cn.nj.storm.shsf.core;

import cn.nj.storm.shsf.rpc.netty.NettyServer;

import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ShsfServerFactory
{
    
    /**
     * TCP 连接池
     */
    public static Map<String, Object> channelMaps;
    
    private NettyServer nettyServer;
    //    private MinaServer minaServer;
    
    public void startServer(String host, int port)
    {
        nettyServer = new NettyServer();
        nettyServer.startServer(host, port);
    }
}
