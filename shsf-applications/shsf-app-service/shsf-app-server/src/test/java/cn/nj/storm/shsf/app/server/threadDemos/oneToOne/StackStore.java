package cn.nj.storm.shsf.app.server.threadDemos.oneToOne;

import java.util.ArrayList;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StackStore<T>
{
    
    private List<T> list;
    
    private int limit = 1;
    
    public StackStore()
    {
        list = new ArrayList<>(limit);
    }
    
    public StackStore(int limit)
    {
        this.limit = limit;
        list = new ArrayList<>(limit);
    }
    
    public synchronized void pushInSafe(T t)
    {
        try
        {
            //如果当前list中
            while (list.size() == 1)
            {
                System.out.println(
                    "push:" + Thread.currentThread().getName() + "@" + Thread.currentThread().getState().name());
                this.wait();
            }
            list.add(t);
            System.out.println("push:" + Thread.currentThread().getName() + "=" + t + "size:" + list.size());
            this.notifyAll();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    public synchronized T popInSafe()
    {
        T value = null;
        try
        {
            //如果当前list中
            while (list.size() == 0)
            {
                System.out.println(
                    "pop:" + Thread.currentThread().getName() + "@" + Thread.currentThread().getState().name());
                this.wait();
            }
            value = list.remove(0);
            System.out.println("pop:" + Thread.currentThread().getName() + "&value=" + value + "&size=" + list.size());
            this.notify();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return value;
    }
    
    public boolean pushUnsafe(T t)
    {
        boolean result = false;
        try
        {
            while (list.size() >= limit)
            {
                System.out.println("push:" + Thread.currentThread().getName() + "@"
                    + Thread.currentThread().getState().name() + "&size:" + list.size());
            }
            result = list.add(t);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public T popUnSafe()
    {
        T value = null;
        try
        {
            synchronized (list){
                //如果当前list中
                while (list.size() == 0)
                {
                    System.out.println(
                            "pop:" + Thread.currentThread().getName() + "@" + Thread.currentThread().getState().name());
                }
                value = list.remove(0);
                System.out.println("pop:" + Thread.currentThread().getName() + "&value=" + value + "&size=" + list.size());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }
}
