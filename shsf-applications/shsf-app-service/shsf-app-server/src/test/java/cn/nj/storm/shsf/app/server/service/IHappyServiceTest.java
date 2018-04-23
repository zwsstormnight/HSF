package cn.nj.storm.shsf.app.server.service;

import cn.nj.storm.App;
import cn.nj.storm.shsf.app.api.service.IHappyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringRunner.class) //14.版本之前用的是SpringJUnit4ClassRunner.class
@SpringBootTest(classes = App.class) //1.4版本之前用的是//@SpringApplicationConfiguration(classes = Application.class)
public class IHappyServiceTest
{
    @Autowired
    private IHappyService iHappyService;

    @Test
    public void makeHappy()
        throws Exception
    {
        
    }
    
    @Test
    public void happyName()
        throws Exception
    {
        iHappyService.happyName();
    }
    
}