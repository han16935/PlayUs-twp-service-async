package com.playus.twpservice.global.config.data.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;

@Configuration
@Profile({"dev", "prod"})  // dev, prod 프로필에서만 사용
public class RedisClusterConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(host, 6379);

        LettuceClientConfiguration lettuceClientConfiguration =
                LettuceClientConfiguration.builder()
                        .build();

        return new LettuceConnectionFactory(
                redisStandaloneConfiguration,
                lettuceClientConfiguration
        );
    }
}
