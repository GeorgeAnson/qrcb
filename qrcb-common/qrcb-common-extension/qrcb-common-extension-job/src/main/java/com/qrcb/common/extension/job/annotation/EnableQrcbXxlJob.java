package com.qrcb.common.extension.job.annotation;

import com.qrcb.common.extension.job.XxlJobAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 开启支持 XXL <br/>
 */

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ XxlJobAutoConfiguration.class })
public @interface EnableQrcbXxlJob {
}
