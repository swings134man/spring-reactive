package com.lucas.movieinfoboot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Sample Controller For Flux and Mono
 * And Also Testing The "Streaming Endpoint"(무한 스트림 API: SSE)
 */
@RestController
public class FluxAndMonoSampleController {

    @GetMapping("/flux")
    public Flux<Integer> fluxSample() {
        return Flux.just(1, 2, 3)
                .log();
    }

    @GetMapping("/mono")
    public Mono<String> monoSample() {
        return Mono.just("This is Mono")
                .log();
    }


    /**
     * SSE(Server-Sent Event)을 이용한 무한 스트림 API
     * 매 1초마다 1씩 증가하는 Flux를 반환
     * @return
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> streamSample() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }
}
