package cn.nj.storm.shsf.core.utils;

import javassist.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JavassistUtils
{
    
    private JavassistUtils()
    {
        
    }
    
    private static ClassPool classPool = ClassPool.getDefault();
    
    public static Class<?> createClass(String className, String interfaceName, Map<Class<?>, String> fields,
        Map<String, Object> methods)
        throws Exception
    {
        // 创建一个类
        CtClass ctClass = classPool.makeClass(className);
        // 为当前类设置接口
        if (StringUtils.isNotBlank(interfaceName))
        {
            ctClass.setInterfaces(new CtClass[] {classPool.get(interfaceName)});
        }
        // 无参构造器
        CtConstructor constructor = new CtConstructor(null, ctClass);
        constructor.setModifiers(Modifier.PUBLIC);
        constructor.setBody("{}");
        ctClass.addConstructor(constructor);
        if (MapUtils.isNotEmpty(fields))
        {
            fields.forEach((fieldType, fieldName) -> {
                // 为类型设置字段
                CtField field = null;
                field.setModifiers(Modifier.PRIVATE);
                // 添加getter和setter方法
                try
                {
                    field = new CtField(classPool.get(fieldType.getName()), fieldName, ctClass);
                    ctClass.addMethod(CtNewMethod.setter("setValue", field));
                    ctClass.addMethod(CtNewMethod.getter("getValue", field));
                    ctClass.addField(field);
                }
                catch (NotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (CannotCompileException e)
                {
                    e.printStackTrace();
                }
            });
        }
        // TODO 参数构造器
        //        constructor = new CtConstructor(new CtClass[] {classPool.get(String.class.getName())}, ctClass);
        //        constructor.setModifiers(Modifier.PUBLIC);
        //        constructor.setBody("{this.value=$1;}");
        //        ctClass.addConstructor(constructor);
        
        // 为类设置方法
        methods.forEach((methodName, methodNameBody) -> {
            CtMethod method = new CtMethod(CtClass.voidType, methodName, null, ctClass);
            method.setModifiers(Modifier.PUBLIC);
            try {
                method.setBody("{System.out.println(\"执行结果\" + this.value);}");
                ctClass.addMethod(method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 加载和执行生成的类
        return ctClass.toClass();
    }
}
