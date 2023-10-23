package com.qrcb.common.core.security.serializer;

import cn.hutool.core.util.StrUtil;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.util.KeyStrResolver;
import com.qrcb.common.core.security.component.QrcbRedisTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description redis token store 自动配置 <br/>
 */

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class QrcbTokenStoreAutoConfiguration {

    private final KeyStrResolver resolver;

    private final RedisConnectionFactory connectionFactory;

    @Bean
    public TokenStore tokenStore() {
        QrcbRedisTokenStore tokenStore = new QrcbRedisTokenStore(connectionFactory, resolver);
        tokenStore.setPrefix(SecurityConstants.QRCB_PREFIX + SecurityConstants.OAUTH_PREFIX);
        tokenStore.setAuthenticationKeyGenerator(new DefaultAuthenticationKeyGenerator() {
            @Override
            public String extractKey(OAuth2Authentication authentication) {
                // 增加租户隔离部分 租户ID:原生计算值
                return resolver.extract(super.extractKey(authentication), StrUtil.COLON);
            }
        });
        return tokenStore;
    }

}
