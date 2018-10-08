package cn.nj.storm.shsf.app.server.threadDemos.connectionPoolDemos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/10/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConnectionPoolTest
{
    
    /**
     * 初始化的连接池，核心线程数限制为10个
     */
    private static ConnectionPool pool = new ConnectionPool(10);
    
    private static CountDownLatch start = new CountDownLatch(1);
    
    private static CountDownLatch end;
    
    public static void main(String[] args)
        throws Exception
    {
        //线程数 这里可以动态修改
        int threadCounts = 10;
        end = new CountDownLatch(threadCounts);
        
        int count = 20;
        AtomicInteger hit = new AtomicInteger();
        AtomicInteger miss = new AtomicInteger();
        
        for (int i = 0; i < threadCounts; i++)
        {
            Thread thread = new Thread(new ConnectionRunner(count, hit, miss), "ConnectionRunnerThread_" + i);
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke:" + (threadCounts * count));
        System.out.println("got connection:" + hit);
        System.out.println("miss connection:" + miss);
    }
    
    static class ConnectionRunner implements Runnable
    {
        
        int count;
        
        AtomicInteger hit;
        
        AtomicInteger miss;
        
        public ConnectionRunner(int count, AtomicInteger hit, AtomicInteger miss)
        {
            this.count = count;
            this.hit = hit;
            this.miss = miss;
        }
        
        @Override
        public void run()
        {
            try
            {
                start.await();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            while (count > 0)
            {
                /**
                 * 从连接池中获取连接，如果1000ms没获得连接，则返回null
                */
                try
                {
                    Connection connection = pool.fetchConnection(1000L);
                    if (connection != null)
                    {
                        try
                        {
                            connection.createStatement();
                            connection.commit();
                        }
                        finally
                        {
                            pool.releaseConnection(connection);
                            hit.incrementAndGet();
                        }
                    }
                    else
                    {
                        //原子操作的递增
                        miss.incrementAndGet();
                    }
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
