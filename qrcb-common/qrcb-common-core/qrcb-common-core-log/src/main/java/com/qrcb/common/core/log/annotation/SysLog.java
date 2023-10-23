package com.qrcb.common.core.log.annotation;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 操作日志注解 <br/>
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 描述
     * @return {String}
     */
    String value();
}
