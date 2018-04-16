package cn.nj.storm.shsf.app.server.zkDemos;

import cn.nj.storm.App;
import cn.nj.storm.shsf.app.server.zkDemos.service.CuratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringRunner.class) //14.版本之前用的是SpringJUnit4ClassRunner.class
@SpringBootTest(classes = App.class) //1.4版本之前用的是//@SpringApplicationConfiguration(classes = Application.class)
public class CuratorServiceTest
{
    @Autowired
    private CuratorService curatorService;

    @Test
    public void controll()
            throws Exception
    {
        curatorService.controll();
    }

    @Test
    public void list()
    {
        List<String> list = curatorService.znodeAll();
        System.out.println(list);
    }

    @Test
    public void listByParent()
    {
        List<String> list = curatorService.znodesByParent("/test");
        System.out.println(list);
    }
}
