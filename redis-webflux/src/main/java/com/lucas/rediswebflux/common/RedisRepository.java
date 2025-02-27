package com.lucas.rediswebflux.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReactiveKeyCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisRepository {

    private final ReactiveRedisTemplate<String, Object> objTemplate;
    private final ReactiveStringRedisTemplate stringTemplate;
    private final ReactiveKeyCommands keyCommands;
    private final ObjectMapper mapper;

    /**
     * Redis Save
     * @param key
     * @param value
     * @return
     * @param <T>
     */
    public <T> Mono<Boolean> save(String key, T value) {
        return objTemplate.opsForValue().set(key, value);
    }

    /**
     * Redis Save Strings(String Template)
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> saveStrings(String key, String value) {
        return stringTemplate.opsForValue().set(key, value);
    }

    /**
     * Redis Get Mono Single Value
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> Mono<T> get(String key, Class<T> clazz) {
        return objTemplate.opsForValue().get(key)
                .map(data -> mapper.convertValue(data, clazz))
                .defaultIfEmpty(null);
    }

    /**
     * Redis Get Mono Single Value(String Template)
     * @param key
     * @return
     */
    public Mono<String> getStrings(String key) {
        return stringTemplate.opsForValue().get(key);
    }


    /**
     * Redis Get Mono By Input Type Value
     * you can use this method when you want to get a value with a specific type. (List<T>, Map<K, V>, etc)
     * @param key
     * @param typeReference
     * @return
     * @param <T>
     */
    public <T> Mono<T> getByType(String key, TypeReference<T> typeReference) {
        return objTemplate.opsForValue().get(key)
                .map(data -> mapper.convertValue(data, typeReference))
                .defaultIfEmpty(null);
    }

    /**
     * Redis Get Mono List<T> Value
     * @param key
     * @param type
     * @return
     * @param <T>
     */
    public <T> Mono<List<T>> getList(String key, TypeReference<List<T>> type) {
        return objTemplate.opsForValue().get(key)
                .map(data -> mapper.convertValue(data, new TypeReference<List<T>>() {}))
                .defaultIfEmpty(Collections.emptyList());
    }

    /**
     * Redis Delete By Key
     * @param key
     * @return
     */
    public Mono<Boolean> deleteByKey(String key) {
        return objTemplate.opsForValue().delete(key);
    }

}//class
