package com.qrcb.common.core.datasource.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description <br/>
 */

public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ClearTtlDsInterceptor()).addPathPatterns("/**");
    }

}