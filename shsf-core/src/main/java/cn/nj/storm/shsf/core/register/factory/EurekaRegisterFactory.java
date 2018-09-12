//package cn.nj.storm.shsf.core.register.factory;
//
//import cn.nj.storm.shsf.core.register.RegisterService;
//import cn.nj.storm.shsf.core.register.impl.ZkRegisterService;
//import cn.nj.storm.shsf.core.utill.Constants;
//
///**
// * <一句话功能简述>
// * <功能详细描述>
// *
// * @author zhengweishun
// * @version [版本号, 2018/9/11]
// * @see [相关类/方法]
// * @since [产品/模块版本]
// */
//public class EurekaRegisterFactory extends AbstractRegisterFactory {
//
//    private EurekaRegisterFactory() {
//    }
//
//    @Override
//    public RegisterService create(String registerCentre) {
//        super.create(registerCentre);
//        if (!registerCentre.equals(Constants.EUREKA)) {
//            return null;
//        }
//        return ZkRegisterService.getInstance(appName);
//    }
//}
