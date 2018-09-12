package cn.nj.storm.shsf.core.utill;

/**
 * <默认常量类>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface Constants
{
    /**
     * 连接超时时间 单位毫秒
     */
    int CONNECTION_TIMEOUT = 3000;
    
    /**
     * 连接超时时间 单位毫秒
     */
    int RETRY_TIMES = 3;
    
    /**
     * 默认名称
     */
    String SHSF_NAME = "shsf";

    /**
     * 本地 不做注册机制
     */
    String LOCAL = "Local";

    /**
     * 数据库注册
     */
    String DB = "Database";

    /**
     * zookeeper注册
     */
    String ZOOKEEPER = "Zookeeper";

    /**
     * redis注册
     */
    String REDIS = "Redis";

    /**
     * Eureka注册
     */
    String EUREKA = "Eureka";

    /**
     * 无中心化
     */
    String CLUSTER = "Cluster";
    
    /**
     * 注册中心类别
     */
    enum CENTRE_TYPE
    {
        LOCAL("local");

        CENTRE_TYPE(String code)
        {
            this.code = code;
        }

        private String code;

        public String getCode()
        {
            return code;
        }

    }

    String PROVIDER = "provider";

    String CONSUMER = "consumer";
}
