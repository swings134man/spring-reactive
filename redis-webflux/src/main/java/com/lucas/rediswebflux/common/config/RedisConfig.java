package com.lucas.rediswebflux.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveServerCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        if(profile.equals("local")) {
            return new LettuceConnectionFactory(host, port);
        }else {
            // TODO: proxy, prod Env = Password Exist
            return new LettuceConnectionFactory(host, port);
        }
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
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }

    @Bean
    public ReactiveServerCommands redisServerCommands(ReactiveRedisConnectionFactory factory) {
        return factory.getReactiveConnection().serverCommands();
    }
}
