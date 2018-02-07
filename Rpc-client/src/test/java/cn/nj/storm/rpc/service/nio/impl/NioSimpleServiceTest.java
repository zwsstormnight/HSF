package cn.nj.storm.rpc.service.nio.impl;

import cn.nj.storm.ClientApplication;
import cn.nj.storm.rpc.service.nio.NioSimpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
public class NioSimpleServiceTest
{
    @Autowired
    private NioSimpleService nioSimpleService;

    @Test
    public void start()
        throws Exception
    {
        String result = nioSimpleService.start();
        System.out.println(result);
    }
    
}