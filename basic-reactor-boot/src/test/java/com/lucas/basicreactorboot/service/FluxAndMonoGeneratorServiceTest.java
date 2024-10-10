package com.lucas.basicreactorboot.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

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


    @Test
    void namesFlux_map_withFiltering() {
        int length = 3;

        var data = generator.namesFlux_map(length);

        StepVerifier.create(data)
//                .expectNext("ALEX", "BEN", "CHLOE") // Not Filtering
//                .expectNext("ALEX", "CHLOE") // Just Filtering
                .expectNext("4-ALEX", "5-CHLOE") // Filtering + Mapping
                .verifyComplete();
    }

    @Test
    void reactive_streams_immutable() {
        var data = generator.namesFlux_immutable();

        StepVerifier.create(data)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void flatMapTestOne() {
        int length = 3;

        var data = generator.namesFlux_flatMap(length);

        StepVerifier.create(data)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void flatMapDelay() {
        int length = 3;
        var data = generator.namesFlux_flatMap_async(length);

        StepVerifier.create(data)
                // Delay 가 걸려있으므로, 각 요소는 순서가 다름. -> flatMap() 은 async 하게 동작함
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E") // 각 요소는 Delay 가 걸려있어서 순서가 다를 수 있음 즉 Test Fail
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void concatMapTest() {
        int length = 3;
        var data = generator.namesFlux_concatMap(length);

        StepVerifier.create(data)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void monoFlatMap() {
        int length = 3;
        var data = generator.namesMono_flatMap(length);

        StepVerifier.create(data)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void mono_flatMapMany() {
        var data = generator.mono_flatMapMany();

        StepVerifier.create(data)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void fluxTransform() {
        int length = 3;

        var data = generator.namesFlux_transform(length);

        StepVerifier.create(data)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void flatMapTestSamples() {
//        Flux<String> userIds = Flux.just("user1", "user2", "user3");
//        Flux<UserData> userDataFlux = userIds
//                .flatMap(userId -> getUserDataFromService(userId)); // 비동기로 각 사용자 데이터를 가져옴

        // 즉 각 사용자에 대한 데이터를 가져오는 비동기 작업을 수행하고, 각 사용자에 대한 데이터를 반환하는 Flux를 생성함.
    }
}