package com.qrcb.common.core.swagger.config;

import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description webflux 网关 swagger 资源路径配置 <br/>
 */

public class WebFluxSwaggerConfiguration implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

}