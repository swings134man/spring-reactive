package com.lucas.rediswebflux.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@TestConfiguration
public class RedisTestConfig {

    private final String host = "127.0.0.1";
    private final int port = 6379;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext
                .<String, Object>newSerializationContext(new StringRedisSerializer()) // Key 직렬화
                .key(new StringRedisSerializer())  // Key 직렬화
                .value(new Jackson2JsonRedisSerializer<>(Object.class)) // Value 직렬화 (JSON)
                .hashKey(new StringRedisSerializer()) // Hash Key 직렬화
                .hashValue(new Jackson2JsonRedisSerializer<>(Object.class)) // Hash Value 직렬화
                .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
