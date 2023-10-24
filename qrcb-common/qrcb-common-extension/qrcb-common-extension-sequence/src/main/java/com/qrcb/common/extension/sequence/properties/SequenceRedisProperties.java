package com.qrcb.common.extension.sequence.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 发号器 Redis 配置属性 <br/>
 */

@Data
@Component
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "qrcb.sequence.redis")
public class SequenceRedisProperties extends BaseSequenceProperties {

    private static final long serialVersionUID = 1L;

}
