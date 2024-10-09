package com.lucas.basicreactorboot.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService generator = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux = generator.namesFlux();

        //then
        // 일반 객체가 아니기에 assert()문 사용불가로 -> reactor-test 를 사용함
        // StepVerifier => Flux, Mono 를 테스트하기 위한 유틸리티 클래스
        // subscribe() 를 호출하지 않아도, create() 함수에서 내부적으로 처리함.(이벤트를{stream}을 보내도록 처리)
        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")// 순서가 다르면 fail
//                .expectNextCount(3) // 순서 상관없이 3개의 값이 있는지 확인
                .expectNext("alex")// 아래와같이 Combine 하여 사용가능
                .expectNextCount(2)// Stream 에서 2개의 값이 남았는지확인
                .verifyComplete();

    }
}