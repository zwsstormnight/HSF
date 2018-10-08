package cn.nj.storm.shsf.app.server.threadDemos;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/10/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ThreadPool<T extends Runnable> {

    /**
     * 执行一个需要实现runnbale接口的任务
     * @param t
     */
    void execute(T t);

    /**
     * 增加工作线程
     * @param num
     */
    void addWorkers(int num);

    /**
     * 减少工作线程
     * @param num
     */
    void removeWorkers(int num);

    /**
     * 关闭线程池
     */
    void shutDown();

    /**
     * 获取正在执行的线程数
     * @return
     */
    int workingSize();
}
