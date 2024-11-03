package com.lucas.movieinfoboot.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * SSE Test 를 위한 Test Class
 */
public class SinkTest {

    @Test
    void sink() {
        /**
         * replay: 여러 이벤트를 저장하고 새로운 Subscriber 가 구독할 때 저장된 이벤트를 모두 전달
         * Sinks 는 수동으로 Signal 을 Trigger 할 수 있게함 -> emitNext() 를 통해서
         * -> asFlux() 를 통해서 SubScribe 할 수 있게함
         */
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        // emitNext() : 이벤트 내보내기 (값, 실패 처리(핸들링))
        // emit 하는동안 실패하면 실패 호출과, 실패처리
        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        // 구독(Subscribing)
        // emit 한 값에 접근하는 방법, 실제 값에 접근가능함.
        Flux<Integer> integerFlux = replaySink.asFlux();
        integerFlux.subscribe(i -> {
            System.out.println("Subscriber 1 : " + i);
        });

        // 둘다 구독이 가능함
        Flux<Integer> integerFlux2 = replaySink.asFlux();
        integerFlux2.subscribe(i -> {
            System.out.println("Subscriber 2 : " + i);
        });

        // 구독자를 추가하고 나서 이벤트를 발생시키면, 구독자들에게 모두 이벤트가 전달됨
        // 1,2 가 모두 publish 된후 3이 추가로 발생하였기에 기존의 구독자들에게 3이라는 값이 전달되는것.
        // tryEmitNext(): 명시적인 오류 처리 제공필요가 없음(내부적으로 이미 존재함)
        replaySink.tryEmitNext(3);

        // 구독자 추가
        // Sinks.replay() -> 구독자가 새로 생기면 모든 이벤트를 전달하는 메서드
        // 1,2,3 이 전송됨
        // many.multicast(): 새로운 구독자가 생기면 이후의 이벤트만 전달하는 메서드
        Flux<Integer> integerFlux3 = replaySink.asFlux();
        integerFlux3.subscribe(i -> {
            System.out.println("Subscriber 3 : " + i);
        });
    }

    @Test
    void sink_multicast() {
        // multicast 사용시 backpressure 옵션을 사용해야한다.
        Sinks.Many<Integer> multicast = Sinks.many().multicast().onBackpressureBuffer();

        multicast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multicast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux = multicast.asFlux();
        integerFlux.subscribe(i -> {
            System.out.println("Subscriber 1 : " + i);
        });

        Flux<Integer> integerFlux1 = multicast.asFlux();
        integerFlux1.subscribe(i -> {
            System.out.println("Subscriber 2 : " + i);
        });

        /**
         * multicast 는 새로운 구독자가 생기면 이후의 이벤트만 전달하는 메서드
         * 1,2 가 전송된후 3이 추가로 발생하였기에 기존의 구독자들에게 3이라는 값이 전달되는것.
         * 또한 새로 추가된 구독자는 1,2 는 전달받지 못하고 3만 전달받음
         */
        multicast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    void sink_unicast() {
        /**
         * unicast: 하나의 Subscriber 만 구독가능 (하나의 구독자만 이벤트를 받을 수 있음)
         * 즉 여기선 다수의 구독자가 있기에 Exception!
         */
        Sinks.Many<Integer> unicast = Sinks.many().unicast().onBackpressureBuffer();

        unicast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        unicast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux = unicast.asFlux();
        integerFlux.subscribe(i -> {
            System.out.println("Subscriber 1 : " + i);
        });

        Flux<Integer> integerFlux1 = unicast.asFlux();
        integerFlux1.subscribe(i -> {
            System.out.println("Subscriber 2 : " + i);
        });

    }
}
