package cn.nj.storm.shsf.app.server.zkDemos.utils;

import java.util.ArrayList;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonUtils {

    public static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds) {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }

    public static String convert2StringResult(Object result) {
        String responses = "";
        if (result == null) {
            return null;
        }
        if (result instanceof ArrayList) {
            responses = result.toString();
        } else if (result instanceof byte[]) {
            responses = new String((byte[]) result);
        } else {
            responses = result.toString();
        }
        return responses;
    }

    public static void print(Object result) {
        System.out.println(convert2StringResult(result));
    }
}
