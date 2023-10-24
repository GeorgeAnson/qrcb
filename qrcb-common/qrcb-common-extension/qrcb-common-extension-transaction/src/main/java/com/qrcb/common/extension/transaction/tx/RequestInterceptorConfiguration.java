package com.qrcb.common.extension.transaction.tx;

import com.qrcb.common.extension.transaction.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description <br/>
 */

@Configuration
public class RequestInterceptorConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new TransactionRestTemplateInterceptor();
    }

}
