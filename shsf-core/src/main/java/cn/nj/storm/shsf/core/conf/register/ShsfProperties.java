package cn.nj.storm.shsf.core.conf.register;

import cn.nj.storm.shsf.core.utils.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/12]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ConfigurationProperties(prefix = "shsf.app")
public class ShsfProperties {

    /**
     * 服务调用协议
     */
    private String protocal = Constants.SHSF_PROTOCAL;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务地址
     */
    private String address;

    /**
     * 服务连接端口
     */
    private String port;
}
