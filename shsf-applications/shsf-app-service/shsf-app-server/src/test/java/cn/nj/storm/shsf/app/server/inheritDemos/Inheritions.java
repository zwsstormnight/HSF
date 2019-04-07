package cn.nj.storm.shsf.app.server.inheritDemos;



/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zwsst
 * @version [版本号, 2019/4/7]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Inheritions {

    public static void main(String[] args) {

    }
}

//泛型也可以继承和实现接口
interface ARB<E> {

}

class Father1<T> {

}

class Child1<T, E> extends Father1<T> implements ARB<E> {

}

class Father2<T1, T2>{
    T1 t1;
    T2 t2;

    public Father2(T1 t1,T2 t2){
        this.t1 = t1;
        this.t2 = t2;
        System.out.println(this.t1.getClass());
        System.out.println(this.t2.getClass());
    }


}
//子类泛型可以比父类多
class Child2<T1, T2, T3> extends Father2<T1, T2> {

    public Child2(T1 t1, T2 t2) {
        super(t1, t2);
    }

}

//部分继承:继承时将父类一个泛型实例化
class Child3<T1, T2, T3> extends Father2<T1,String> {

    public Child3(T1 t1, String t2) {
        super(t1, t2);
    }

}

//子类将父类全部实现，子类独有，不再是继承的
class Child4<T1, T2, T3> extends Father2<Integer,String> {

    public Child4(Integer t1, String t2) {
        super(t1, t2);
    }
}

//不实现父类泛型 父类所有成员默认为Object类型
class Child5<T1, T2, T3> extends Father2{

    public Child5(Object t1, Object t2) {
        super(t1, t2);
    }
}