package cn.nj.storm.shsf.core.register;

/**
 * <服务注册器工厂>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IRegisterFactory {

    /**
     * 创建注册业务实例
     * @param registerCentre
     * @return
     */
    RegisterService create(String registerCentre);

}
