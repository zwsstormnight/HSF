package cn.nj.storm.shsf.app.server.threadDemos;

import cn.nj.storm.shsf.app.server.threadDemos.oneToOne.*;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <线程的生产者消费者模式>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProduceAndShsfConsumerDemos
{
    /**
     * 一个生产者 一个消费者
     */
    @Test
    public void one2one()
    {
        StackStore<String> stringStackStore = new StackStore<>();
        ProducerOne producerOne = new ProducerOne(stringStackStore);
        ConsumerOne consumerOne = new ConsumerOne(stringStackStore);
        ProducerThread pt = new ProducerThread(producerOne);
        ConsumerThread ct = new ConsumerThread(consumerOne);
        pt.start();
        ct.start();
        try
        {
            Thread.sleep(100000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 一个生产者 一个消费者
     */
    @Test
    public void one2n()
    {
        StackStore<String> stringStackStore = new StackStore<>();
        ProducerThread pt = new ProducerThread(new ProducerOne(stringStackStore));
        pt.start();
        ExecutorService service = Executors.newCachedThreadPool();
        ConsumerThread ct1 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct2 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct3 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct4 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct5 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ct1.start();
        ct2.start();
        ct3.start();
        ct4.start();
        ct5.start();
        try
        {
            Thread.sleep(100000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void n2one()
    {
        StackStore<String> stringStackStore = new StackStore<>();
        ProducerThread pt1 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt2 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt3 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt4 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt5 = new ProducerThread(new ProducerOne(stringStackStore));
        pt1.start();
        pt2.start();
        pt3.start();
        pt4.start();
        pt5.start();
        ConsumerThread ct1 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ct1.start();
        try
        {
            Thread.sleep(100000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void n2n()
    {
        StackStore<String> stringStackStore = new StackStore<>();
        ProducerThread pt1 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt2 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt3 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt4 = new ProducerThread(new ProducerOne(stringStackStore));
        ProducerThread pt5 = new ProducerThread(new ProducerOne(stringStackStore));
        pt1.start();
        pt2.start();
        pt3.start();
        pt4.start();
        pt5.start();
        ConsumerThread ct1 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct2 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct3 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct4 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ConsumerThread ct5 = new ConsumerThread(new ConsumerOne(stringStackStore));
        ct1.start();
        ct2.start();
        ct3.start();
        ct4.start();
        ct5.start();
        try
        {
            Thread.sleep(100000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void dynamic()
    {
        StackStore<String> stringStackStore = new StackStore<>(10);
        try
        {
            ConsumerThread ct1 = new ConsumerThread(new ConsumerOne(stringStackStore));
            ct1.start();
            for (int i = 0; i < 10; i++)
            {
                stringStackStore.pushUnsafe("" + i);
                Thread.sleep(1000L);
            }
            Thread.sleep(100000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + ":" + thread.isDaemon());
        //        thread.setDaemon(true);
        //        System.out.println(thread.getName()+":"+thread.isDaemon());
        Thread t1 = new Thread(() -> {
            System.out.println(".....!!!!");
            try {
                Thread.sleep(1000000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.setDaemon(true);
        t1.start();
        System.out.println(t1.getName()+":"+t1.getState().name());
        System.out.println("end...");
    }
}
