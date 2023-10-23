package com.qrcb.common.core.security.annotation;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description 服务调用鉴权注解 <br/>
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

    /**
     * 是否AOP统一处理
     *
     * @return false, true
     */
    boolean value() default true;

    /**
     * 需要特殊判空的字段(预留)
     *
     * @return {}
     */
    String[] field() default {};

}
