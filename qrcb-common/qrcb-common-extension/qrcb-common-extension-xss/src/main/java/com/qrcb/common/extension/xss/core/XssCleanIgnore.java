package com.qrcb.common.extension.xss.core;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 忽略 xss <br/>
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssCleanIgnore {
}
