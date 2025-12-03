package com.playus.twpservice.global.config.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

@Configuration
@Profile({"local", "test"})
@ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }


//    @Bean
//    @Qualifier("chatRedisTemplate")
//    public RedisTemplate<String, ChattingMessage> chatRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, ChattingMessage> redisTemplate = new RedisTemplate<>();
//
//        ObjectMapper objectMapper = new ObjectMapper()
//                .registerModule(new JavaTimeModule())
//                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//
//        Jackson2JsonRedisSerializer<ChattingMessage> serializer =
//                new Jackson2JsonRedisSerializer<>(objectMapper, ChattingMessage.class);
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
//
//        return redisTemplate;
//    }

    @Bean
    @Qualifier("chatRoomRedisTemplate")
    public RedisTemplate<String, Object> chatRoomRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
