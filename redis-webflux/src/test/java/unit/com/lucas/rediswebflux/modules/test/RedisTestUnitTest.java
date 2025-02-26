package com.lucas.rediswebflux.modules.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.rediswebflux.config.RedisTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis Object Test Unit Class
 */
@DataRedisTest
@Import(RedisTestConfig.class)
class RedisTestUnitTest {

    @Autowired
    private ReactiveRedisTemplate<String, Object> template;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void flush() {
        template.getConnectionFactory().getReactiveConnection()
                .serverCommands().flushAll();
    }

    @Test
    @DisplayName("List<String> - Object")
    @Deprecated // objectMapper 를 사용하면 2중 직렬화
    void list_test() {
        // given
        List<String> list = List.of("1", "2", "3", "4", "5");

        // when
        template.opsForValue().set("list", list).block();

        // then
        List<String> res = template.opsForValue().get("list")
                .map(data -> (List<String>) data)
                .doOnNext(result -> assertEquals(list, result))
                .block();

        res.stream().forEach(System.out::println);
    }

    @Test
    @DisplayName("List<String> - ObjectMapper - 잘못된 테스트")
    @Deprecated // ObjectMapper 를 사용하면 2중직렬화가 되기에 사용 X -> Type Casting 시에만 사용할것
    void list_mapper() throws JsonProcessingException {
        // given
        List<String> list = List.of("1", "2", "3", "4", "5");

        // when
        template.opsForValue().set("list", mapper.writeValueAsString(list)).block();

        // then
        List res = template.opsForValue().get("list")
                .flatMap(data -> {
                    try {
                        return Mono.just(mapper.readValue(data.toString(), List.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return Mono.error(e);
                    }
                })
                .block();

        res.stream().forEach(System.out::println);
    }

    // Type Casting 올바른 방법
    @Test
    @DisplayName("List<String> - get Type Casting ObjectMapper")
    void get_list_mapper() {
        // given
        List<String> list = List.of("1", "2", "3", "4", "5");

        // when
        template.opsForValue().set("list", list).block();

        // then
        Mono<List<String>> res = template.opsForValue().get("list")
                .map(obj -> new ObjectMapper().convertValue(obj, new TypeReference<List<String>>() {
                }));

        List<String> block = res.block();
        System.out.println(block);
        System.out.println("Type: " + (block != null ? block.getClass() : "null"));

        assertNotNull(block);
        assertEquals(List.of("1", "2", "3", "4", "5"), block);
    }


}