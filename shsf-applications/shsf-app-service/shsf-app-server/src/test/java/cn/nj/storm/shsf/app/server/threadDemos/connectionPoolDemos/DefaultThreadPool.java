package cn.nj.storm.shsf.app.server.threadDemos.connectionPoolDemos;

import cn.nj.storm.shsf.app.server.threadDemos.ThreadPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/10/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DefaultThreadPool<T extends Runnable> implements ThreadPool<T> {

    /**
     * 线程池最大限制数
     */
    private static final int MAX_WORKER_NUMBERS = 10;

    /**
     * 线程池初始化的默认线程数
     */
    private static final int DEFAULT_WORKER_NUMBERS = 5;

    /**
     * 线程池最小线程数
     */
    private static final int MIN_WORKER_NUMBERS = 1;

    /**
     * 工作队列
     */
    private final LinkedList<T> tasks = new LinkedList<>();

    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

    private AtomicLong threadNum = new AtomicLong();

    @Override
    public void execute(T t) {

    }

    @Override
    public void addWorkers(int num) {

    }

    @Override
    public void removeWorkers(int num) {

    }

    @Override
    public void shutDown() {

    }

    @Override
    public int workingSize() {
        return 0;
    }

    class Worker implements Runnable{

//        private
        /**
         * 是否工作
         */
        private volatile boolean running = true;

        @Override
        public void run() {
            while(running){
                synchronized (tasks){

                }
            }
        }
    }
}
