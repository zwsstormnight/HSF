package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.annotation.RpcProviderService;
import cn.nj.storm.shsf.core.conf.CuratorClientConfig;
import cn.nj.storm.shsf.core.utill.LoggerInterface;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * <zookeeper注册实现>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ZkRegisterService extends AbstractRegisterService implements LoggerInterface {
    @Value("${service.name.space}")
    private String namespace;

    @Value("${app.serverLists}")
    private String appAddress;

    @Autowired
    private ApplicationContext applicationContext;

    public ZkRegisterService() {

    }

    public ZkRegisterService(String registerRoot) {
        if (StringUtils.isBlank(namespace)) {
            namespace = registerRoot;
        }
    }

    @Override
    public String register(String packageName) {
        CuratorFramework curatorFramework = applicationContext.getBean(CuratorFramework.class);
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcProviderService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            try {
                for (Object serviceBean : serviceBeanMap.values()) {
                    String interfaceName =
                            serviceBean.getClass().getAnnotation(RpcProviderService.class).value().getName();
                    //节点路径
                    String interFaceIdNode = "/" + interfaceName;
                    //无当前节点
                    if (curatorFramework.checkExists().forPath(interFaceIdNode) == null) {
                        //创建的路径和值
                        curatorFramework.create()
                                .creatingParentsIfNeeded()
                                .withMode(CreateMode.PERSISTENT)
                                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                                .forPath(interFaceIdNode);
                    }
                    //节点路径
                    String ipNode = interFaceIdNode + "/" + appAddress;
                    if (curatorFramework.checkExists().forPath(ipNode) != null) {
                        //有当前IP的接点，则删除后，重新建立
                        curatorFramework.delete().forPath(ipNode);
                    }
                    //创建的路径和值
                    curatorFramework.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.EPHEMERAL)
                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                            .forPath(ipNode);
                }
            } catch (Exception e) {
                runLogger.error("create zookeeper node failure", e);
            }
        }
        return null;
    }
}
