package com.qrcb.common.core.swagger.annotation;

import com.qrcb.common.core.swagger.config.GatewaySwaggerAutoConfiguration;
import com.qrcb.common.core.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 开启 swagger <br/>
 */

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableSwagger2
@Import({ SwaggerAutoConfiguration.class, GatewaySwaggerAutoConfiguration.class })
public @interface EnableQrcbSwagger2 {
}
