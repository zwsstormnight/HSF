package cn.nj.storm.rpc.service.zk;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <一句话功能简述>
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
    private static final String ZK_PATH = "/zktest";

    @Autowired
    private CuratorFramework client;

    @Override
    public void controll()
    {
        try
        {
//            Thread.sleep(20000L);
//            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            // 1.Connect to zk
//            CuratorFramework client = CuratorFrameworkFactory.newClient(zkList, 5000, 3000, retryPolicy);

//            client.start();
//            System.out.println("zk client start successfully!");

            // 2.Client API test
            // 2.1 Create node
            String data1 = "hello";
            print("create", ZK_PATH, data1);
            client.create().creatingParentsIfNeeded().forPath(ZK_PATH, data1.getBytes());

            // 2.2 Get node and data
            print("ls", "/");
            print(client.getChildren().forPath("/"));
            print("get", ZK_PATH);
            print(client.getData().forPath(ZK_PATH));

            // 2.3 Modify data
            String data2 = "world";
            print("set", ZK_PATH, data2);
            client.setData().forPath(ZK_PATH, data2.getBytes());
            print("get", ZK_PATH);
            print(client.getData().forPath(ZK_PATH));

            // 2.4 Remove node
            print("delete", ZK_PATH);
            client.delete().forPath(ZK_PATH);
            print("ls", "/");
            print(client.getChildren().forPath("/"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void print(String... cmds)
    {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds)
        {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }

    private static void print(Object result)
    {
        System.out.println(result instanceof byte[] ? new String((byte[])result) : result);
    }
}
