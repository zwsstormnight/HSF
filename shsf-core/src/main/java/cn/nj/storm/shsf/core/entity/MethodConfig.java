package cn.nj.storm.shsf.core.entity;

import lombok.Data;

/**
 * <方法对象实体>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class MethodConfig {

    private String name;

    private Class<?> returnType;


}
