package cn.nj.storm.shsf.core.register.impl;

import cn.nj.storm.shsf.core.conf.register.ShsfProperties;
import cn.nj.storm.shsf.core.utill.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * <zookeeper注册实现>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service(value = Constants.ZOOKEEPER)
@Slf4j
public class ZkRegisterService extends AbstractRegisterService
{
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ShsfProperties shsfProperties;
    
    @Override
    public String register(String appName, String appAddress)
    {
        if (MapUtils.isEmpty(regMap))
        {
            return null;
        }
        CuratorFramework curatorFramework = applicationContext.getBean(CuratorFramework.class);
        regMap.forEach((serviceName, serviceValue) -> {
            serviceValue.forEach((role, serviceDetail) -> {
                String rolePath = "/" + serviceName + "/" + role;
                try
                {
                    if (curatorFramework.checkExists().forPath(rolePath) == null)
                    {
                        curatorFramework.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                            .forPath(rolePath, null);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                serviceDetail = shsfProperties.getProtocal() + "://"
                    + ObjectUtils.defaultIfNull(shsfProperties.getAddress(), appAddress) + ":"
                    + shsfProperties.getPort() + "?" + serviceDetail;
                String detailPath = rolePath + "/" + ObjectUtils.defaultIfNull(shsfProperties.getAddress(), appAddress)
                    + ":" + shsfProperties.getPort();
                try
                {
                    curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(detailPath, serviceDetail.getBytes());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        });
        return null;
    }
}
