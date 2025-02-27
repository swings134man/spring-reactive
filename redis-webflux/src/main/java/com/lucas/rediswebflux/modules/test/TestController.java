package com.lucas.rediswebflux.modules.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/test")
public class TestController {

    private final TestService service;

    @PostMapping("/string")
    public Mono<ResponseEntity<Boolean>> saveString(@RequestBody KeyValueDto dto) {
        return service.saveString(dto.getKey(), dto.getValue())
                .map(data -> ResponseEntity.ok().body(data));
    }

    @GetMapping("/string/{key}")
    public Mono<ResponseEntity<String>> getByKeyString(@PathVariable("key") String key) {
        return service.getByKeyString(key)
                .map(data -> ResponseEntity.ok().body(data));
    }

    // ---------------------------- Object ----------------------------
    @PostMapping("/obj")
    public Mono<ResponseEntity<Boolean>> saveObj(@RequestBody KeyValueDto dto) throws JsonProcessingException {
        return service.saveObj(dto.getKey(), dto.getValue())
                .map(data -> ResponseEntity.ok().body(data));
    }

    @GetMapping("/obj/{key}")
    public Mono<ResponseEntity<String>> getByKeyObjString(@PathVariable("key") String key) {
        return service.getObjToString(key)
                .map(data -> ResponseEntity.ok().body(data));
    }

    @PostMapping("/dto")
    public Mono<ResponseEntity<Boolean>> saveObj(@RequestBody TestObjDTO dto) {
        return service.saveObj(dto)
                .map(data -> ResponseEntity.ok().body(data));
    }

    @GetMapping("/dto/{id}")
    public Mono<ResponseEntity<TestObjDTO>> getObjDto(@PathVariable("id") Long id) {
        return service.getObjDto(id)
                .map(data -> ResponseEntity.ok().body(data));
    }


    @GetMapping("/flush")
    public Mono<ResponseEntity<String>> flushAll() {
        return service.flushAll()
                .map(data -> ResponseEntity.ok().body(data));
    }
}
