package com.qrcb.common.extension.datahub;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description datahub配置 <br/>
 */

@Data
@Component
@ConfigurationProperties(prefix = "qrcb.datahub")
public class DataHubProperties {

    /**
     * endpoint
     */
    private String endpoint;
    /**
     * account
     */
    private String accessId;
    /**
     * password
     */
    private String accessKey;

    /**
     * 全局通道数量，默认=1
     */
    private Integer globalConcurrency = 1;

}
