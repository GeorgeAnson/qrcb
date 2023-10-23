package com.qrcb.common.core.security.social;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 微信登录配置 <br/>
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "social.wx")
public class WxSocialConfig {

    private String appid;

    private String secret;

}
