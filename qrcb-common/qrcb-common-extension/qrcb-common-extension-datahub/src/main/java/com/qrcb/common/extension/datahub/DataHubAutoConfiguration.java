package com.qrcb.common.extension.datahub;

import com.aliyun.datahub.client.DatahubClient;
import com.aliyun.datahub.client.DatahubClientBuilder;
import com.aliyun.datahub.client.auth.AliyunAccount;
import com.aliyun.datahub.client.common.DatahubConfig;
import com.aliyun.datahub.client.http.HttpConfig;
import com.aliyun.datahub.clientlibrary.consumer.Consumer;
import com.qrcb.common.extension.datahub.annotation.DataHubListenerAnnotationProcessor;
import com.qrcb.common.extension.datahub.config.ConcurrentMessageListenerContainerFactory;
import com.qrcb.common.extension.datahub.config.DataHubListenerEndpointRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description datahub 自动配置 <br/>
 */

@Configuration
@ConditionalOnClass(Consumer.class)
public class DataHubAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConcurrentMessageListenerContainerFactory containerFactory(DataHubProperties dataHubProperties) {
        ConcurrentMessageListenerContainerFactory concurrent = new ConcurrentMessageListenerContainerFactory();
        concurrent.setConcurrency( dataHubProperties.getGlobalConcurrency() );
        return concurrent;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataHubListenerAnnotationProcessor dataHubListenerAnnotationProcessor(DataHubProperties dataHubProperties) {
        return new DataHubListenerAnnotationProcessor( dataHubProperties );
    }

    @Bean
    @ConditionalOnMissingBean
    public DataHubListenerEndpointRegistry dataHubListenerEndpointRegistry() {
        return new DataHubListenerEndpointRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public DatahubClient datahubClient(DataHubProperties dataHubProperties) {
        return DatahubClientBuilder.newBuilder().setDatahubConfig(
                new DatahubConfig( dataHubProperties.getEndpoint(),
                        new AliyunAccount( dataHubProperties.getAccessId(),
                                dataHubProperties.getAccessKey() ),
                        true
                )
        ).setHttpConfig( new HttpConfig()
                .setConnTimeout( 10000 )
                .setCompressType( HttpConfig.CompressType.LZ4 )
        ).build();
    }

}
