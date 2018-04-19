package cn.nj.storm.shsf.core.entity;

/**
 * <服务配置对象>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ServiceConfig {

    private String name;

    private String valueName;

    private Class<?> clazz;

    private int retries;

    private int timeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "name='" + name + '\'' +
                ", valueName='" + valueName + '\'' +
                ", clazz=" + clazz +
                ", retries=" + retries +
                ", timeout=" + timeout +
                '}';
    }
}
