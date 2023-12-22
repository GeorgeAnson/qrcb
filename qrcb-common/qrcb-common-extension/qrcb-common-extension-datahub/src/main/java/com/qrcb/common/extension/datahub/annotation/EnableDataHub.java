package com.qrcb.common.extension.datahub.annotation;

import com.qrcb.common.extension.datahub.DataHubAutoConfiguration;
import com.qrcb.common.extension.datahub.DataHubProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DataHubAutoConfiguration.class)
@EnableConfigurationProperties(DataHubProperties.class)
public @interface EnableDataHub {
}
