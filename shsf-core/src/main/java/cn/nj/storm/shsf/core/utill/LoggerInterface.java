package cn.nj.storm.shsf.core.utill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/4/15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface LoggerInterface
{
    Logger operationLogger = LoggerFactory.getLogger("operation");

    Logger interfaceLogger = LoggerFactory.getLogger("interface");

    Logger interface_headerLogger = LoggerFactory.getLogger("interface_header");

    Logger debugLogger = LoggerFactory.getLogger("debug");

    Logger runLogger = LoggerFactory.getLogger("run");
}
