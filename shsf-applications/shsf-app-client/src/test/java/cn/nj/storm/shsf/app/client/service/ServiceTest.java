package cn.nj.storm.shsf.app.client.service;

import cn.nj.storm.ClientApplication;
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
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
public class ServiceTest {

    @Autowired
    private BaseService baseService;

    @Test
    public void doTest(){
        baseService.toMakeHappy("zzzz");
    }
}
