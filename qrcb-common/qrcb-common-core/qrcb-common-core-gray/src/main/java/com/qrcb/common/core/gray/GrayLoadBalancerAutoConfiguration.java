package com.qrcb.common.core.gray;

import com.qrcb.common.core.gray.feign.GrayFeignRequestInterceptor;
import com.qrcb.common.core.gray.rule.GrayLoadBalancerClientConfiguration;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description <br/>
 */

@Configuration
@ConditionalOnProperty(value = "gray.rule.enabled", havingValue = "true")
@LoadBalancerClients(defaultConfiguration = GrayLoadBalancerClientConfiguration.class)
public class GrayLoadBalancerAutoConfiguration {

    @Bean
    public RequestInterceptor grayFeignRequestInterceptor() {
        return new GrayFeignRequestInterceptor();
    }

}