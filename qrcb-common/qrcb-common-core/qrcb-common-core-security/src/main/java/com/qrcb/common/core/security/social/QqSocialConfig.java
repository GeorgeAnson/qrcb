package com.qrcb.common.core.security.social;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description qq 登录配置信息 <br/>
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "social.qq")
public class QqSocialConfig {

    private String appid;

    private String secret;

}
