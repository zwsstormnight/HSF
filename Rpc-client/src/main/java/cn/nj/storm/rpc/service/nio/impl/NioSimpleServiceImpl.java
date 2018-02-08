package cn.nj.storm.rpc.service.nio.impl;

import cn.nj.storm.rpc.service.nio.NioSimpleService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/2/6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class NioSimpleServiceImpl implements NioSimpleService
{
    @Override
    public String start()
    {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = null;
        try
        {
            socketChannel = SocketChannel.open();
            /**
             * 设置 非阻塞式的信道
             * 在非阻塞式信道上调用一个方法总是会立即返回。这种调用的返回值指示了所请求的操作完成的程度。
             * 例如，在一个非阻塞式ServerSocketChannel上调用accept()方法，如果有连接请求来了，则返回客户端SocketChannel，否则返回null。
             */
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
            
            if (socketChannel.finishConnect())
            {
                int i = 0;
                /**
                 * 注意SocketChannel.write()方法的调用是在一个while循环中的。Write()方法无法保证能写多少字节到SocketChannel。
                 * 所以，我们重复调用write()直到Buffer没有要写的字节为止。
                 * 非阻塞模式下,read()方法在尚未读取到任何数据时可能就返回了。
                 * 所以需要关注它的int返回值，它会告诉你读取了多少字节。
                 */
                while (true)
                {
                    Thread.sleep(2000);
                    //消息体
                    String info = "I'm " + i++ + "-th information from client";
                    buffer.clear();
                    buffer.put(info.getBytes());
                    buffer.flip();
                    while (buffer.hasRemaining())
                    {
                        System.out.println(buffer);
                        socketChannel.write(buffer);
                    }
                }
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (socketChannel != null)
                {
                    socketChannel.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
