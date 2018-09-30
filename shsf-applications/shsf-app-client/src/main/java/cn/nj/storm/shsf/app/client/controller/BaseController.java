package cn.nj.storm.shsf.app.client.controller;

import cn.nj.storm.shsf.app.client.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/9/26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    @Autowired
    private BaseService baseService;

    @PostMapping(value = "/make/happy")
    public String md1(){
        return baseService.toMakeHappy("ada");
    }
}
