package com.lucas.basicreactorboot.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Flux, Mono Generator Service -> include main() Methods
 */
public class FluxAndMonoGeneratorService {

        // Flux Type
        // log() -> Flux 에서 제공, publisher, subscriber 간의 통신에서 일어나는 Events 을 확인할 수 있음(onNext, onComplete, onError)
        // log() 내부에서 request(unbounded) -> 무한대 요청을 뜻함(1 ~ n)
        public Flux<String> namesFlux() {
            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .log(); //db or a remote service call
        }

        // Mono Type
        public Mono<String> nameMono() {
            return Mono.just("alex");
        }


    public static void main(String[] args) {
        System.out.println("@@@@ FluxAndMonoGeneratorService.main() @@@@");

        FluxAndMonoGeneratorService famgs = new FluxAndMonoGeneratorService(); // instance

        /**
         * subscribes -> Flux 값에 접근하기 위함.
         * 해당 값에 접근하려면, subscribe() 메소드를 호출해야 함. -> 해당 elements 접근가능
         * (Elements 가 Stream 형태로 하나씩 전송됨.)
         *
         * Subscribe 하지 않으면 아무것도 일어나지 않음. (Flux 에서 Data 전송 X)
         */
        famgs.namesFlux()
                .subscribe(name -> {
                    System.out.println("(Flux)Name Is : " + name);
                });

        // Mono
        famgs.nameMono()
                .subscribe(name -> {
                    System.out.println("(Mono)Name Is : " + name);
                });


    }//main
}//class
