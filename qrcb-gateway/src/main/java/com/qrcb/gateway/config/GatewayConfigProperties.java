package com.qrcb.gateway.config;

import com.qrcb.gateway.filter.PasswordDecoderFilter;
import com.qrcb.gateway.filter.ValidateCodeGatewayFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 网关通用配置文件 <br/>
 */

@Data
@RefreshScope
@Configuration
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

    /**
     * 网关解密登录前端密码 秘钥 {@link PasswordDecoderFilter}
     */
    public String encodeKey;

    /**
     * 网关不需要校验验证码的客户端 {@link ValidateCodeGatewayFilter}
     */
    public List<String> ignoreClients;
}
