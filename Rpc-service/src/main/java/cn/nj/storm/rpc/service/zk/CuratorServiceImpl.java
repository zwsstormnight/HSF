package cn.nj.storm.rpc.service.zk;

import cn.nj.storm.rpc.util.CommonUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <curator zk客户端接口实现>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service("curatorService")
public class CuratorServiceImpl implements CuratorService
{
    public static Logger logger = LoggerFactory.getLogger("run");

    private static final String ZK_PATH = "/zktest";
    
    @Autowired
    private CuratorFramework client;
    
    @Override
    public void controll()
    {
        try
        {
            // RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            // 1.Connect to zk
            // CuratorFramework client = CuratorFrameworkFactory.newClient(zkList, 5000, 3000, retryPolicy);
            // client.start();
            // System.out.println("zk client start successfully!");
            
            // 2.Client API test
            // 2.1 Create node
            String data1 = "hello";
            CommonUtils.print("create", ZK_PATH, data1);
            client.create().creatingParentsIfNeeded().forPath(ZK_PATH, data1.getBytes());
            
            // 2.2 Get node and data
            CommonUtils.print("ls", "/");
            CommonUtils.print(client.getChildren().forPath("/"));
            CommonUtils.print("get", ZK_PATH);
            CommonUtils.print(client.getData().forPath(ZK_PATH));
            
            // 2.3 Modify data
            String data2 = "world";
            CommonUtils.print("set", ZK_PATH, data2);
            client.setData().forPath(ZK_PATH, data2.getBytes());
            CommonUtils.print("get", ZK_PATH);
            CommonUtils.print(client.getData().forPath(ZK_PATH));
            
            // 2.4 Remove node
            CommonUtils.print("delete", ZK_PATH);
            client.delete().forPath(ZK_PATH);
            CommonUtils.print("ls", "/");
            CommonUtils.print(client.getChildren().forPath("/"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<String> znodeAll()
    {
        CommonUtils.print("ls", "/");
        try
        {
            String result = CommonUtils.convert2StringResult(client.getChildren().forPath("/"));
            CommonUtils.print(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<String> znodesByParent(String parent)
    {
        CommonUtils.print("ls", parent);
        return null;
    }
}
