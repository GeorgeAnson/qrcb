package com.qrcb.common.extension.xss;

import com.qrcb.common.extension.xss.config.QrcbXssProperties;
import com.qrcb.common.extension.xss.core.FormXssClean;
import com.qrcb.common.extension.xss.core.JacksonXssClean;
import com.qrcb.common.extension.xss.core.XssCleanInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description jackson xss 配置 <br/>
 */

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(QrcbXssProperties.class)
@ConditionalOnProperty(value = "security.xss.enabled", havingValue = "true", matchIfMissing = true)
public class QrcbXssAutoConfiguration implements WebMvcConfigurer {

    private final QrcbXssProperties xssProperties;

    @Bean
    public FormXssClean formXssClean() {
        return new FormXssClean();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {
        return builder -> builder.deserializerByType(String.class, new JacksonXssClean());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
        registry.addInterceptor(interceptor).addPathPatterns(xssProperties.getPathPatterns())
                .excludePathPatterns(xssProperties.getExcludePatterns()).order(Ordered.LOWEST_PRECEDENCE);
    }

}
