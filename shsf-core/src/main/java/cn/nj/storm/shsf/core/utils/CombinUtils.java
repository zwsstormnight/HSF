package cn.nj.storm.shsf.core.utils;

/**
 * <组合工具类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CombinUtils
{
    private CombinUtils()
    {
    }
    
    /**
     * 首字母转小写
     * @param s 要转变首字母为小写的字符串
     */
    public static String toLowerCaseFirstOne(String s)
    {
        if (Character.isLowerCase(s.charAt(0)))
        {
            return s;
        }
        else
        {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    
    /**
     * 首字母转大写
     * @param s 要转变首字母为大写的字符串
     */
    public static String toUpperCaseFirstOne(String s)
    {
        if (Character.isUpperCase(s.charAt(0)))
        {
            return s;
        }
        else
        {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
