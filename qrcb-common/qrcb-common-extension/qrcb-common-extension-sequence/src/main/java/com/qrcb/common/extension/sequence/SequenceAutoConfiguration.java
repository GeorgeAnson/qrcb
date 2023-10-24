package com.qrcb.common.extension.sequence;

import com.qrcb.common.extension.sequence.builder.SnowflakeSeqBuilder;
import com.qrcb.common.extension.sequence.properties.SequenceSnowflakeProperties;
import com.qrcb.common.extension.sequence.sequence.Sequence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 序列号自动配置 <br/>
 */

@Configuration
@ComponentScan("com.qrcb.common.extension.sequence")
@ConditionalOnMissingBean(Sequence.class)
public class SequenceAutoConfiguration {

    /**
     * snowflake 算法作为发号器实现
     *
     * @param properties {@link SequenceSnowflakeProperties}
     * @return {@link Sequence}
     */
    @Bean
    @ConditionalOnBean(SequenceSnowflakeProperties.class)
    public Sequence snowflakeSequence(SequenceSnowflakeProperties properties) {
        return SnowflakeSeqBuilder.create().datacenterId(properties.getDataCenterId())
                .workerId(properties.getWorkerId()).build();
    }
}
