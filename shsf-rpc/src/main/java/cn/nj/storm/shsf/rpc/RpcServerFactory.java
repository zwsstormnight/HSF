package cn.nj.storm.shsf.rpc;

import cn.nj.storm.shsf.rpc.netty.NettyClient;
import cn.nj.storm.shsf.rpc.netty.NettyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RpcServerFactory
{
    
    /**
     * TCP 连接池
     */
    public static Map<String, Object> channelMaps;
    
    public static List<String> list = new ArrayList<>();

    private Map<String, Map<String, String>> consumerMap;
    
    private NettyServer nettyServer;
    //    private MinaServer minaServer;
    
    public void startServer(int port)
    {
        nettyServer = new NettyServer();
        nettyServer.startServer(port);
        //从订阅的服务创建nettyClient
        NettyClient nettyClient = new NettyClient();
        this.consumerMap = consumerMap;
    }
    
}
