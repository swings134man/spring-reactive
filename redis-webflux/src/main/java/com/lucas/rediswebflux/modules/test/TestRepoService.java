package com.lucas.rediswebflux.modules.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lucas.rediswebflux.common.RedisRepository;
import com.lucas.rediswebflux.modules.test.entity.TestObjDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @package : com.lucas.rediswebflux.modules.test
 * @name : TestRepoService.java
 * @date : 2025. 2. 27. 오후 4:03
 * @author : lucaskang(swings134man)
 * @Description: Redis 공통 Repository RedisRepository.class 사용 Class
**/
@Service
@RequiredArgsConstructor
@Slf4j
public class TestRepoService {

    private final RedisRepository redisRepository;

    private static final String KEY = "dto:";

    // ---------------------------- Strings ----------------------------
    public Mono<Boolean> saveString(String key, String value) {
        return redisRepository.saveStrings(key, value)
                .doOnNext(data -> log.info("saveString = {}", data));
    }

    public Mono<String> getByKeyString(String key) {
        return redisRepository.getStrings(key)
                .doOnNext(value -> log.info("getByKey = {}:{}", key, value));
    }

    // ---------------------------- Object ----------------------------
    // ---------------------------- Type ----------------------------
    public Mono<Boolean> saveDto(TestObjDTO dto) {
        return redisRepository.save(KEY + dto.getId(), dto);
    }

    public Mono<Boolean> saveDtoList(List<TestObjDTO> dtoList) {
        return redisRepository.save(KEY + "list", dtoList);
    }

    public Mono<TestObjDTO> getDto(Long id) {
        return redisRepository.get(KEY + id, TestObjDTO.class)
                .doOnNext(data -> log.info("getDto = {}", data));
    }

    // TODO: Redis 의 Key 전부를 가져올 일은 거의 존재하지 않음(참고만 하는 코드), 단지 Type 을 지정해서 가져오는 경우 사용
    public Mono<TestObjDTO> getDtoByType(Long id) {
        return redisRepository.getByType(KEY + id, new TypeReference<TestObjDTO>() {})
                .doOnNext(data -> log.info("getDtoByType = {}", data));
    }

    public Mono<List<TestObjDTO>> getDtoList(Long id) {
        return redisRepository.getList(KEY + id, new TypeReference<List<TestObjDTO>>() {})
                .doOnNext(data -> log.info("getDtoList = {}", data));
    }

    public Mono<Boolean> deleteDtoByKey(Long id) {
        return redisRepository.deleteByKey(KEY + id);
    }
}
