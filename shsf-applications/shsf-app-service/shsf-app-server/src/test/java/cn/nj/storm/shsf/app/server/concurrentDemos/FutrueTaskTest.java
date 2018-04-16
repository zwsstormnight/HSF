package cn.nj.storm.shsf.app.server.concurrentDemos;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FutrueTaskTest {
    public static void main(String[] args)
    {
        ExecutorService pool = null;
        try
        {
            pool = Executors.newCachedThreadPool();
            //Future用于异步线程获取返回结果
            pool.submit(new Thread2());
            Thread mainThread = new Thread(new Runnable(){
                @Override
                public void run()
                {
                    Thread childThread = new Thread(new Thread3());
                    childThread.setDaemon(true);
                    childThread.start();
                    System.out.println("I'm main thread...");
                }
            });
            mainThread.start();
            //            pool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pool != null)
            {
                pool.shutdown();
            }
        }
    }
}

class Thread1 implements Callable
{

    @Override
    public Object call()
            throws Exception
    {
        System.out.println("Thread1");
        return new Exception("Thread1");
    }
}

class Thread2 implements Runnable
{
    @Override
    public void run()
    {
        System.out.println("Thread2");
//        throw new Exception("Thread2");
    }
}

class Thread3 implements Runnable
{
    @Override
    public void run()
    {
        try
        {
            System.out.println("Thread3");
            TimeUnit.MILLISECONDS.sleep(1000);
//            throw new Exception("Thread3");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

