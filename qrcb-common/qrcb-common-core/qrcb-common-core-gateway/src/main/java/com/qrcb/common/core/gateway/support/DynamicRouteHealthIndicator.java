package com.qrcb.common.core.gateway.support;

import com.qrcb.common.core.assemble.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 动态路由健康检查 <br/>
 */

@Slf4j
@RequiredArgsConstructor
public class DynamicRouteHealthIndicator extends AbstractHealthIndicator {

    private final RedisTemplate redisTemplate;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        if (redisTemplate.hasKey(CacheConstants.ROUTE_KEY)) {
            builder.up();
        }
        else {
            log.warn("动态路由健康检查失败，当前无路由配置");
            builder.down();
        }
    }

}
