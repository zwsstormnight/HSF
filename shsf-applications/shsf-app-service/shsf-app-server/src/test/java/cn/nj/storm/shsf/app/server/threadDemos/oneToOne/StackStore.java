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
    
    private List<T> list = new ArrayList<>();

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
            System.out.println(
                    "push:" + Thread.currentThread().getName() + "=" + t);
            this.notifyAll();
            System.out.println("push=" + list.size());
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
            System.out.println(
                    "pop:" + Thread.currentThread().getName() + "=" + value);
            this.notify();
            System.out.println("pop=" + list.size());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return value;
    }
}
