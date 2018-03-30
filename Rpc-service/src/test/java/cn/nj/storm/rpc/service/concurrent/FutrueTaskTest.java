package cn.nj.storm.rpc.service.concurrent;

import java.util.concurrent.*;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/3/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FutrueTaskTest
{
    public static void main(String[] args)
    {
        ExecutorService pool = null;
        try
        {
            pool = Executors.newCachedThreadPool();
            //Future用于异步线程获取返回结果
            pool.submit(new Thread2());
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
        return new Exception("Thread1");
    }
}

class Thread2 implements Runnable
{
    @Override
    public void run()
    {
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
            throw new Exception("Thread3");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
