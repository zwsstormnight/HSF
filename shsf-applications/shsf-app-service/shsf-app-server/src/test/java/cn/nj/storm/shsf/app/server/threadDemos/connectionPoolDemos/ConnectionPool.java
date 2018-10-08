package cn.nj.storm.shsf.app.server.threadDemos.connectionPoolDemos;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/10/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConnectionPool {

    private LinkedList<Connection> pool = new LinkedList<>();

    public ConnectionPool(int initialSize){
        if(initialSize > 0){
            for(int i = 0; i < initialSize; i++){
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection){
        if(connection != null){
            synchronized (pool){
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    /**
     * 设置获取连接超时
     * @param timeout 超时时间 单位是毫秒
     * @return
     */
    public Connection fetchConnection(long timeout) throws InterruptedException {
        synchronized (pool){
            if(timeout <= 0){
                while(pool.isEmpty()){
                    //释放锁
                    pool.wait();
                }
                //获取第一个链接
                return pool.removeFirst();
            }else{
                long future = System.currentTimeMillis() + timeout;
                long remaining = timeout;
                while(pool.isEmpty() && remaining > 0){
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection respConnect = null;
                if(!pool.isEmpty()){
                    respConnect = pool.removeFirst();
                }
                return respConnect;
            }
        }
    }
}
