package com.qrcb.common.extension.oss;

import com.qrcb.common.extension.oss.endpoint.OssEndpoint;
import com.qrcb.common.extension.oss.service.OssTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description aws 自动配置类 <br/>
 */

@AllArgsConstructor
@EnableConfigurationProperties({ OssProperties.class })
public class OssAutoConfiguration {

    private final OssProperties properties;

    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnProperty(name = "oss.enable", havingValue = "true", matchIfMissing = true)
    public OssTemplate ossTemplate() {
        return new OssTemplate(properties);
    }

    @Bean
    @ConditionalOnProperty(name = "oss.info", havingValue = "true")
    public OssEndpoint ossEndpoint(OssTemplate template) {
        return new OssEndpoint(template);
    }

}
