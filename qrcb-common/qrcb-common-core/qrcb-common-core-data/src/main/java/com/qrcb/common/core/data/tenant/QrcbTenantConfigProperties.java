package com.qrcb.common.core.data.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 多租户配置 <br/>
 */

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "qrcb.tenant")
public class QrcbTenantConfigProperties {


    /**
     * 维护租户列名称
     */
    private String column = "tenant_id";

    /**
     * 多租户的数据表集合
     */
    private List<String> tables = new ArrayList<>();
}
