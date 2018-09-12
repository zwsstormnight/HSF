package cn.nj.storm.shsf.core.conf.zookeeper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ConfigurationProperties(prefix = "shsf.zookeeper")
public class ZookeeperProperties {

    private List<String> serverLists;
}
