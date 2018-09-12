package cn.nj.storm.shsf.app.server.concurrentDemos;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/5/1]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class WaitTest
{
    public static void main(String[] args)
    {
        String strObj = "";
        try {
            System.out.println("同步方法前");
            synchronized (WaitTest.class){
                strObj.wait();
                System.out.println("同步方法中");
            }
            System.out.println("同步方法后");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
