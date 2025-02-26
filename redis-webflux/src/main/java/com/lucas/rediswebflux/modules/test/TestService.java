package com.lucas.rediswebflux.modules.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveServerCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ReactiveRedisTemplate<String, Object> objectTemplate;
    private final ReactiveServerCommands redisServerCommands;
    private final ObjectMapper objectMapper;

    public Mono<Boolean> saveString(String key, String value) {
        log.info("saveString = {}:{}", key, value);
        return redisTemplate.opsForValue().set(key, value);
    }

    public Mono<String> getByKeyString(String key) {
        return redisTemplate.opsForValue().get(key)
                .doOnNext(value -> log.info("getByKey = {}:{}", key, value));
    }


    // ---------------------------- Object ----------------------------

    public Mono<Boolean> saveObj(String key, String value) throws JsonProcessingException {
        log.info("saveObj = {}:{}", key, value);
        return objectTemplate.opsForValue().set(key, value);
    }

    public Mono<String> getObjToString(String key) {
        return objectTemplate.opsForValue().get(key)
//                .cast(String.class) // value 가 String 이 아닐경우 Exception
                .map(Object::toString)
                .doOnNext(value -> log.info("getByObjKey = {}:{}", key, value))
                .defaultIfEmpty("Empty");
    }

    public Mono<String> flushAll() {
        return redisServerCommands.flushAll()
                .doOnSuccess(s -> log.info("Flush All"));
    }
}
