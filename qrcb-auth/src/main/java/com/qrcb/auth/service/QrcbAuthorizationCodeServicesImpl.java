package com.qrcb.auth.service;

import com.qrcb.common.core.assemble.constant.SecurityConstants;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Service;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 授权码模式支持集群 <br/>
 */

@Service
@RequiredArgsConstructor
public class QrcbAuthorizationCodeServicesImpl extends RandomValueAuthorizationCodeServices {

    private final RedisConnectionFactory connectionFactory;

    @Setter
    private String prefix = SecurityConstants.QRCB_PREFIX + SecurityConstants.OAUTH_CODE_PREFIX;

    @Setter
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    /**
     * 保存 code 和 认证信息
     *
     * @param code           授权码模式： code
     * @param authentication 认证信息
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        @Cleanup
        RedisConnection connection = connectionFactory.getConnection();
        connection.set(serializationStrategy.serialize(prefix + code), serializationStrategy.serialize(authentication));
    }

    /**
     * 删除code 并返回认证信息
     *
     * @param code 授权码模式： code
     * @return {@link OAuth2Authentication}
     */
    @Override
    protected OAuth2Authentication remove(String code) {
        @Cleanup
        RedisConnection connection = connectionFactory.getConnection();

        byte[] key = serializationStrategy.serialize(prefix + code);
        byte[] value = connection.get(key);

        if (value == null) {
            return null;
        }

        OAuth2Authentication oAuth2Authentication = serializationStrategy.deserialize(value,
                OAuth2Authentication.class);

        connection.del(key);
        return oAuth2Authentication;
    }
}
