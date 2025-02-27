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

    private static final String KEY = "dto:";

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

    // Get Request 경우 getObjToString 도 사용가능(String)
    public Mono<Boolean> saveObj(TestObjDTO dto) {
        log.info("saveObj = {}", dto);
        return objectTemplate.opsForValue().set(KEY + dto.getId(), dto);
    }

    public Mono<TestObjDTO> getObjDto(Long id) {
        String dtoKey = KEY + id;
        return objectTemplate.opsForValue().get(dtoKey)
                .map(data -> objectMapper.convertValue(data, TestObjDTO.class))
                .doOnNext(dto -> log.info("getObjDto = {}", dto))
                .defaultIfEmpty(new TestObjDTO());
    }

    public Mono<String> flushAll() {
        return redisServerCommands.flushAll()
                .doOnSuccess(s -> log.info("Flush All"));
    }
}
