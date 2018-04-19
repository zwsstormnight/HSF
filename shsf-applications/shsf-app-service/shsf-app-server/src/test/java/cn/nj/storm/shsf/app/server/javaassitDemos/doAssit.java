package cn.nj.storm.shsf.app.server.javaassitDemos;

import javassist.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class doAssit {

    public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        // TODO Auto-generated method stub
        // 创建类
        ClassPool pool = ClassPool.getDefault();
        CtClass cls = pool.makeClass("com.situ.super.Sclass");
        try {
            // 添加私有成员name及其getter、setter方法
            CtField param = new CtField(pool.get("java.lang.String"), "name", cls); //相当于private String name
            param.setModifiers(Modifier.PRIVATE);  //私有修饰
            cls.addMethod(CtNewMethod.setter("setName", param));//增加set方法，名字为"setName"
            cls.addMethod(CtNewMethod.getter("getName", param));//增加get方法，名字为getname
            cls.addField(param, CtField.Initializer.constant("")); //写入class文件

            // 添加无参的构造体
            CtConstructor cons = new CtConstructor(new CtClass[] {}, cls);  //相当于public Sclass(){this.name = "brant";}
            cons.setBody("{name = \"Brant\";}");
            cls.addConstructor(cons);

            // 添加有参的构造体
            cons = new CtConstructor(new CtClass[] {pool.get("java.lang.String")}, cls);//把参数列表写在本行
            cons.setBody("{$0.name = $1;}");  //第一个传入的形参$1,$2第二个传入的形参，相当于public Sclass(String s){this.name = s;}
            cls.addConstructor(cons);

            //把生成的class文件写入文件,也可以不写入
//            byte[] byteArr = ctClass.toBytecode();
//            FileOutputStream fos = new FileOutputStream(new File("D://Emp.class"));
//            fos.write(byteArr);
//            fos.close();

            //=======================================================上面是关键，创建一个新类，底下是一般的反射调用
            // 通过反射创建无参的实例，并调用getName方法
            Object o = Class.forName("com.situ.super.Sclass").newInstance();  //为了防止编译器报错，先用o声明，并一直使用Class.forName来获取类文件
            Method getter = o.getClass().getMethod("getName");
            System.out.println(getter.invoke(o));

            // 调用其setName方法
            Method setter = o.getClass().getMethod("setName", new Class[] {String.class});
            setter.invoke(o, "Adam");
            System.out.println(getter.invoke(o));

            // 通过反射创建有参的实例，并调用getName方法
            Object oo = Class.forName("com.situ.super.Sclass").getConstructor(String.class).newInstance("Liu Jian");  //调用构造有参函数
            getter = o.getClass().getMethod("getName");
            System.out.println(getter.invoke(o));

        } catch (CannotCompileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 打印创建类的类名
        try {
            System.out.println(cls.toClass());
        } catch (CannotCompileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
