package com.qrcb.common.core.gateway.config;

import com.qrcb.common.core.gateway.filter.GrayReactiveLoadBalancerClientFilter;
import com.qrcb.common.core.gateway.rule.GrayLoadBalancer;
import com.qrcb.common.core.gateway.rule.VersionGrayLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 灰度负载均衡自动配置 <br/>
 */

@Configuration
@EnableConfigurationProperties(LoadBalancerProperties.class)
@ConditionalOnProperty(value = "gray.rule.enabled", havingValue = "true")
@AutoConfigureBefore(GatewayReactiveLoadBalancerClientAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GrayLoadBalancerClientConfiguration {

    @Bean
    public ReactiveLoadBalancerClientFilter gatewayLoadBalancerClientFilter(GrayLoadBalancer grayLoadBalancer,
                                                                            LoadBalancerProperties properties, GatewayLoadBalancerProperties loadBalancerProperties) {
        return new GrayReactiveLoadBalancerClientFilter(loadBalancerProperties, properties, grayLoadBalancer);
    }

    @Bean
    public GrayLoadBalancer grayLoadBalancer(DiscoveryClient discoveryClient) {
        return new VersionGrayLoadBalancer(discoveryClient);
    }
}
