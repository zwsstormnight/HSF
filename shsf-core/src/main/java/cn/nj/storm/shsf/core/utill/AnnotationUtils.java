package cn.nj.storm.shsf.core.utill;

import org.reflections.Reflections;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * <注解工具类>
 * <依赖 >
 * <dependency>
 * <groupId>org.reflections</groupId>
 * <artifactId>reflections</artifactId>
 * <version>0.9.10</version>
 * </dependency>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AnnotationUtils {

    /**
     * 获取指定目录下的指定注解的类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getServices(String packageName, Class clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(clazz);
        for (Class<?> serviceClass : classesList) {
            System.out.println(serviceClass.getAnnotation(clazz));
            System.out.println("000000000000000000");
        }

        return classesList;
    }
}
