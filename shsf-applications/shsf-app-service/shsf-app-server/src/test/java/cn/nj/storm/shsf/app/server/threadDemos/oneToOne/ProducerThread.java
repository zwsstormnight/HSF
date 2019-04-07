package cn.nj.storm.shsf.app.server.threadDemos.oneToOne;

import cn.nj.storm.shsf.app.server.threadDemos.ShsfProducer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProducerThread extends Thread
{
    private ShsfProducer shsfProducer;
    
    public ProducerThread(ShsfProducer shsfProducer)
    {
        super();
        this.shsfProducer = shsfProducer;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            shsfProducer.produce("" + System.currentTimeMillis());
        }
    }
}