package com.qrcb.common.core.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 租户信息拦截 <br/>
 */

@Configuration
public class QrcbTenantConfiguration {

    @Bean
    public RequestInterceptor qrcbFeignTenantInterceptor() {
        return new QrcbFeignTenantInterceptor();
    }

    @Bean
    public ClientHttpRequestInterceptor qrcbTenantRequestInterceptor() {
        return new TenantRequestInterceptor();
    }

}
