package cn.nj.storm.shsf.rpc.proxy;

import cn.nj.storm.shsf.rpc.protocal.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RpcProxy {

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setClassName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);

//                        if (serviceDiscovery != null) {
//                            serverAddress = serviceDiscovery.discover();
//                        }
//                        if(serverAddress != null){
//                            String[] array = serverAddress.split(":");
//                            String host = array[0];
//                            int port = Integer.parseInt(array[1]);
//
//                            RpcClient client = new RpcClient(host, port);
//                            RpcResponse response = client.send(request);
//
//                            if (response.isError()) {
//                                throw new RuntimeException("Response error.",new Throwable(response.getError()));
//                            } else {
//                                return response.getResult();
//                            }
//                        }
//                        else{
//                            throw new RuntimeException("No server address found!");
//                        }
                    return null;
                }
        );
    }
}
