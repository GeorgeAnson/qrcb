package com.qrcb.common.core.data.cache;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description RedisTemplate 配置 <br/>
 */

@EnableCaching
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisTemplateConfig {


    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


//    @Bean
//    @ConditionalOnProperty(value = "spring.redis.cluster.enable", havingValue = "true")
//    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties) {
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
//        redisClusterConfiguration.setPassword(redisProperties.getPassword());
//
//        // https://github.com/lettuce-io/lettuce-core/wiki/Redis-Cluster#user-content-refreshing-the-cluster-topology-view
//        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
//                .enablePeriodicRefresh()
//                .enableAllAdaptiveRefreshTriggers()
//                .refreshPeriod(Duration.ofSeconds(5))
//                .build();
//
//        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
//                .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(5)))
//                .topologyRefreshOptions(clusterTopologyRefreshOptions).build();
//
//        // https://github.com/lettuce-io/lettuce-core/wiki/ReadFrom-Settings
//        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
//                .readFrom(ReadFrom.REPLICA_PREFERRED)
//                .clientOptions(clusterClientOptions).build();
//
//        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
//    }

}
