package cn.nj.storm.shsf.app.server.nioDemos;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NioServer
{
    private Selector selector;
    
    public NioServer(Selector selector)
    {
        this.selector = selector;
    }
    
    private static final int BUF_SIZE = 1024;
    
    private static final int TIMEOUT = 3000;
    
    public void execute()
    {
        new Worker().start();
    }
    
    /**
     *
     */
    private class Worker extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                channalListener();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void handleAccept(SelectionKey key)
        throws IOException
    {
        ServerSocketChannel ssChannel = (ServerSocketChannel)key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
    }
    
    public static void handleRead(SelectionKey key)
        throws IOException
    {
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer buf = (ByteBuffer)key.attachment();
        System.out.println(sc.isConnected());
        long bytesRead = sc.read(buf);
        if (bytesRead == -1)
        {
            System.out.println("断开..." + sc.socket().getRemoteSocketAddress());
            sc.close();
            return;
        }
        while (bytesRead > 0)
        {
            buf.flip();
            System.out.println("buffer start");
            while (buf.hasRemaining())
            {
                System.out.print((char)buf.get());
            }
            System.out.println("      buffer end");
            buf.clear();
            bytesRead = sc.read(buf);
        }
    }
    
    public static void handleWrite(SelectionKey key)
        throws IOException
    {
        ByteBuffer buf = (ByteBuffer)key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel)key.channel();
        while (buf.hasRemaining())
        {
            sc.write(buf);
        }
        buf.compact();
    }
    
    private void channalListener()
        throws IOException
    {
        while (true)
        {
            /**
             * 调用选择器的select()方法。该方法会阻塞等待，直到有一个或更多的信道准备好了I/O操作或等待超时
             * select()方法将返回可进行I/O操作的信道数量。
             * 现在，在一个单独的线程中，通过调用select()方法就能检查多个信道是否准备好进行I/O操作。
             * 如果经过一段时间后仍然没有信道准备好，select()方法就会返回0，并允许程序继续执行其他任务。
             */
            /*
             * 每次使用select时必须判断下selector是否已经被关闭，否则会报如下错误
             * Exception in thread "pool-1-thread-1" java.nio.channels.ClosedSelectorException at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:83)
             */
            if (!selector.isOpen())
            {
                System.out.println("selector is closed");
                break;
            }
            if (selector.select(TIMEOUT) == 0)
            {
                //每3秒阻塞一次 如果没有信道准备好就循环等待
                System.out.println(Thread.currentThread().getName() + ":" + selector.toString());
                continue;
            }
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext())
            {
                SelectionKey key = iter.next();
                try
                {
                    if (key.isAcceptable())
                    {
                        handleAccept(key);
                    }
                    if (key.isReadable())
                    {
                        handleRead(key);
                    }
                    if (key.isWritable() && key.isValid())
                    {
                        handleWrite(key);
                    }
                    if (key.isConnectable())
                    {
                        System.out.println("isConnectable = true");
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    if (key != null)
                    {
                        key.cancel();
                        key.channel().close();
                    }
                }
                iter.remove();
            }
        }
    }
}
