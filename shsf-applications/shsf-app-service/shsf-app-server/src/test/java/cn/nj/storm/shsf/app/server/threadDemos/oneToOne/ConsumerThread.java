package cn.nj.storm.shsf.app.server.threadDemos.oneToOne;

import cn.nj.storm.shsf.app.server.threadDemos.ShsfConsumer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConsumerThread extends Thread
{
    
    private ShsfConsumer shsfConsumer;
    
    public ConsumerThread(ShsfConsumer<String> shsfConsumer)
    {
        super();
        this.shsfConsumer = shsfConsumer;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            System.out.println("start pop:" + System.currentTimeMillis());
            System.out.println(shsfConsumer.pop());
            System.out.println("end pop:" + System.currentTimeMillis());
        }
    }
}
