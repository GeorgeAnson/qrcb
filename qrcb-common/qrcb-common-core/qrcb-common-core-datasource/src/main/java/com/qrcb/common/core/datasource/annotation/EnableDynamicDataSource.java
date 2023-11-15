package com.qrcb.common.core.datasource.annotation;

import com.qrcb.common.core.datasource.DynamicDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 开启动态数据源 <br/>
 */

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableAutoConfiguration
@Import(DynamicDataSourceAutoConfiguration.class)
public @interface EnableDynamicDataSource {
}
