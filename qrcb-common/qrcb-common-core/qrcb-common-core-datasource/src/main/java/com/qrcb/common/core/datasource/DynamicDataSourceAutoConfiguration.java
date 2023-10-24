package com.qrcb.common.core.datasource;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.qrcb.common.core.datasource.config.DruidDataSourceProperties;
import com.qrcb.common.core.datasource.config.JdbcDynamicDataSourceProvider;
import com.qrcb.common.core.datasource.config.LastParamDsProcessor;
import com.qrcb.common.core.datasource.config.WebMvcConfig;
import lombok.AllArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 动态路由自动配置 <br/>
 */

@Configuration
@AllArgsConstructor
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

    private final StringEncryptor stringEncryptor;

    private final DruidDataSourceProperties properties;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        return new JdbcDynamicDataSourceProvider(stringEncryptor, properties);
    }

    @Bean
    public DsProcessor dsProcessor() {
        return new LastParamDsProcessor();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfig();
    }

}
