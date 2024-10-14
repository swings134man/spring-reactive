package com.lucas.movieinfoboot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit(단위) 테스트의 경우 어떤 Controller 를 Test 할것인지에 대한 controller 를 명시해줘야 한다.
 *
 * Endpoint 를 테스트하기위해 Spring MVC: test rest template, WebFlux: WebTestClient 를 사용한다.
 */
@WebFluxTest(controllers = FluxAndMonoSampleController.class)
@AutoConfigureWebTestClient // WebFlux App Test 자동구성(컨트롤러 엔드포인트 접근,테스트 가능)
class FluxAndMonoSampleControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("1. /Flux Sample Test")
    void fluxSample() {
        webTestClient
                .get()
                    .uri("/flux")
                .exchange()
                .expectStatus()
                    .is2xxSuccessful()
                    .expectBodyList(Integer.class)
                    .hasSize(3);
    }

    @Test
    @DisplayName("2. /Flux Sample Test 2 - StepVerifier 사용")
    void fluxSample_2() {
        var flux = webTestClient
                    .get()
                        .uri("/flux")
                    .exchange()
                    .expectStatus()
                        .is2xxSuccessful()
                            .returnResult(Integer.class)
                            .getResponseBody(); // 완전한 응답 본문 접근가능

        StepVerifier.create(flux)
                .expectNext(1,2,3)
                .verifyComplete();
    }

    @Test
    @DisplayName("3. /Flux Sample Test 3 - consumeWith")
    void fluxSample_3() {
        webTestClient
            .get()
                .uri("/flux")
            .exchange()
            .expectStatus()
                .is2xxSuccessful()
                    .expectBodyList(Integer.class)
                    .consumeWith(listEntityExchangeResult -> { // 응답 본문 접근가능
                        var responseBody = listEntityExchangeResult.getResponseBody();
                        assert Objects.requireNonNull(responseBody).size() == 3;
                    });
    }


    @Test
    @DisplayName("4. /Mono Sample Test")
    void monoSample() {
        webTestClient
                .get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> { // 응답 본문 접근가능
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals("This is Mono", responseBody);
                });
    }

    // Stream 을 통해 계속 데이터가 전송되는 경우
    @Test
    @DisplayName("5. /Flux Sample Test 5 Streaming - StepVerifier 사용")
    void fluxSample_Streaming() {
        var flux = webTestClient
                .get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody(); // 완전한 응답 본문 접근가능

        StepVerifier.create(flux)
                .expectNext(0L, 1L,2L,3L)
                .thenCancel()// 특정값 이후 스트림 종료 cancel()
                .verify();
    }
}