package com.qrcb.common.extension.datahub.annotation;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataHubListener {
    String project();

    String topic();

    String subId();

    String beanRef() default "__listener";

    String containerFactory() default "containerFactory";

    String id() default "";

    String concurrency() default "1";

    String autoStartup() default "true";
}
