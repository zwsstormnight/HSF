package cn.nj.storm.rpc.service.reflect;


import java.lang.reflect.Method;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/3/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Prox
{
    private static String invokeNames = "cn.nj.storm.rpc.service.reflect.Prox-compute";
    
    private static String method = "cn.nj.storm.rpc.service.reflect.Prox.compute";
    
    public int compute(int a, int b)
    {
        System.out.println("start");
        int c = a + b;
        System.out.println("end");
        return c;
    }
    
    public static void main(String[] args)
    {
        try
        {
            //用“-”我是为了写的快 不然应该用下面那个 你要做分割 取最后一个和前面的都要拼接和截取
            String[] names = Prox.invokeNames.split("-");
            Class<?> clazz = Class.forName(names[0]);
            Method mth = clazz.getMethod(names[1], int.class,int.class);
            Object result = mth.invoke(clazz.newInstance(), 1, 2);
            System.out.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
