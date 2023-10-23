package com.qrcb.common.core.feign;

import com.qrcb.common.core.feign.endpoint.FeignClientEndpoint;
import feign.Feign;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.QrcbFeignClientsRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description feign 自动化配置 <br/>
 */

@Configuration
@ConditionalOnClass(Feign.class)
@Import(QrcbFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class QrcbFeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
        return new FeignClientEndpoint(context);
    }

}
