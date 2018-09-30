package cn.nj.storm.shsf.rpc.async;

import cn.nj.storm.shsf.rpc.netty.NettyClient;
import cn.nj.storm.shsf.rpc.protocal.ShsfRequest;
import cn.nj.storm.shsf.rpc.protocal.ShsfResponse;
import cn.nj.storm.shsf.rpc.proxy.RPCCallback;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class ShsfFutureResponse implements Future<Object>
{
    private Sync sync;
    
    private ShsfRequest request;
    
    private ShsfResponse response;
    
    private long startTime;
    
    private long responseTimeThreshold = 5000;
    
    private List<RPCCallback> pendingCallbacks = new ArrayList<>();
    
    private ReentrantLock lock = new ReentrantLock();
    
    private ThreadPoolExecutor threadPoolExecutor;
    
    public ShsfFutureResponse(ShsfRequest request, ThreadPoolExecutor threadPoolExecutor)
    {
        this.request = request;
        this.threadPoolExecutor = threadPoolExecutor;
    }
    
    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isCancelled()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isDone()
    {
        return false;
    }
    
    @Override
    public Object get()
        throws InterruptedException, ExecutionException
    {
        sync.acquire(-1);
        if (this.response != null)
        {
            return this.response.getResult();
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public Object get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException
    {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success)
        {
            if (this.response != null)
            {
                return this.response.getResult();
            }
            else
            {
                return null;
            }
        }
        else
        {
            throw new RuntimeException(
                "Timeout exception. Request id: " + this.request.getRequestId() + ". Request class name: "
                    + this.request.getClassName() + ". Request method: " + this.request.getMethodName());
        }
    }
    
    public void done(ShsfResponse reponse)
    {
        this.response = reponse;
        sync.release(1);
        invokeCallbacks();
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold)
        {
            log.warn("Service response time is too slow. Request id = " + reponse.getRequestId() + ". Response Time = "
                + responseTime + "ms");
        }
    }
    
    private void invokeCallbacks()
    {
        lock.lock();
        try
        {
            for (final RPCCallback callback : pendingCallbacks)
            {
                runCallback(callback);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public ShsfFutureResponse addCallback(RPCCallback callback)
    {
        lock.lock();
        try
        {
            if (isDone())
            {
                runCallback(callback);
            }
            else
            {
                this.pendingCallbacks.add(callback);
            }
        }
        finally
        {
            lock.unlock();
        }
        return this;
    }
    
    private void runCallback(final RPCCallback callback)
    {
        final ShsfResponse res = this.response;
        threadPoolExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                if (StringUtils.isBlank(res.getError()))
                {
                    callback.success(res.getResult());
                }
                else
                {
                    callback.fail(new RuntimeException("Response error", new Throwable(res.getError())));
                }
            }
        });
    }
    
    static class Sync extends AbstractQueuedSynchronizer
    {
        
        private static final long serialVersionUID = 1L;
        
        private final int done = 1;
        
        private final int pending = 0;
        
        @Override
        protected boolean tryAcquire(int arg)
        {
            return getState() == done;
        }
        
        @Override
        protected boolean tryRelease(int arg)
        {
            if (getState() == pending)
            {
                if (compareAndSetState(pending, done))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
        
        public boolean isDone()
        {
            getState();
            return getState() == done;
        }
    }
}
