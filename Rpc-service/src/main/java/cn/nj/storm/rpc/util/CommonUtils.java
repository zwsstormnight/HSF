package cn.nj.storm.rpc.util;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/1/28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonUtils
{
    public static void print(String... cmds)
    {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds)
        {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }
    
    public static String convert2StringResult(Object result)
    {
        return result instanceof byte[] ? new String((byte[])result) : (String)result;
    }
    
    public static void print(Object result)
    {
        System.out.println(convert2StringResult(result));
    }
}
