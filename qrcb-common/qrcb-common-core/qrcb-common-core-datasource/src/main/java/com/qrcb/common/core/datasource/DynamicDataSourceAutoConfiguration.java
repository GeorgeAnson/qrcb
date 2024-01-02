package com.qrcb.common.core.datasource;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceCreatorAutoConfiguration;
import com.qrcb.common.core.datasource.aop.JdbcDynamicDataSourceAnnotationAdvisor;
import com.qrcb.common.core.datasource.aop.JdbcDynamicDataSourceAnnotationInterceptor;
import com.qrcb.common.core.datasource.config.HikariDataSourceProperties;
import com.qrcb.common.core.datasource.config.WebMvcConfig;
import com.qrcb.common.core.datasource.processor.LastParamDsProcessor;
import com.qrcb.common.core.datasource.provider.JdbcDynamicDataSourceProvider;
import lombok.AllArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 动态路由自动配置 <br/>
 */

@Configuration
@AllArgsConstructor
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(value = {HikariDataSourceProperties.class})
@Import(value = DynamicDataSourceCreatorAutoConfiguration.class)
public class DynamicDataSourceAutoConfiguration {

    private final StringEncryptor stringEncryptor;

    private final HikariDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public JdbcDynamicDataSourceProvider jdbcDynamicDataSourceProvider() {
        return new JdbcDynamicDataSourceProvider(stringEncryptor, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        LastParamDsProcessor lastParamDsProcessor = new LastParamDsProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(lastParamDsProcessor);
        lastParamDsProcessor.setNextProcessor(spelExpressionProcessor);
        return headerProcessor;
    }

    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public JdbcDynamicDataSourceAnnotationAdvisor jdbcDynamicDataSourceAnnotationAdvisor(DataSourceCreator dataSourceCreator) {
        JdbcDynamicDataSourceAnnotationInterceptor interceptor = new JdbcDynamicDataSourceAnnotationInterceptor(dataSourceCreator, jdbcDynamicDataSourceProvider());
        interceptor.setDsProcessor(dsProcessor());
        JdbcDynamicDataSourceAnnotationAdvisor advisor = new JdbcDynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfig();
    }

}
