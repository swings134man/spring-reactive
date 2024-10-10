package com.lucas.basicreactorboot.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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

        // Flux Operator - map(), filter()
        public Flux<String> namesFlux_map(int length) {
            // filtering the String whose length is greater than 3
            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .map(String::toUpperCase)
                    .filter(name -> name.length() > length)
                    .map(s -> s.length() + "-" + s) // 문자열의 길이 + 원래문자열
                    .log();
        }

        // Reactive Streams are immutable
        // 반응형 스트림은 불변이다.
        public Flux<String> namesFlux_immutable() {
            var flux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

            // Transforming Flux
            flux.map(String::toUpperCase); // 원래의 Flux(반응형 스트림)은 불변이기에 변하지 않음.

            return flux;
        }

        // FlatMap()
        public Flux<String> namesFlux_flatMap(int length) {
            // 개별 문자를 반환 -> 각 결과를 char 로 반환함 ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .map(String::toUpperCase)
                    .filter(name -> name.length() > length)
                    .flatMap(s -> splitString(s)) // 각 문자열을 분리하여 반환
                    .log();
        }

        public Mono<List<String>> namesMono_flatMap(int length) {
            return Mono.just("alex")
                    .map(String::toUpperCase)
                    .filter(name -> name.length() > length)
                    .flatMap(this::splitStringMono).log(); // Mono<List Of A,L,E,X>
        }



        // FlatMap() - async(비동기): delay 가 걸려있는 method 활용
        public Flux<String> namesFlux_flatMap_async(int length) {
            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .map(String::toUpperCase)
                    .filter(name -> name.length() > length)
                    .flatMap(s -> splitStringWithDelay(s))
                    .log();
        }


        public Flux<String> namesFlux_concatMap(int length) {
            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .map(String::toUpperCase)
                    .filter(name -> name.length() > length)
                    .concatMap(s -> splitStringWithDelay(s))
                    .log();
        }

        public Flux<String> mono_flatMapMany() {
            return Mono.just("alex")
                    .map(String::toUpperCase)
                    .flatMapMany(this::splitString)
                    .log();
        }

        public Flux<String> namesFlux_transform(int length) {

            Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                    .filter(s -> s.length() > length);

            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .transform(filterMap)
                    .flatMap(s -> splitString(s))
                    .defaultIfEmpty("default")
                    .log();
        }

        public Flux<String> namesFlux_transform_switchDefault(int length) {

            Function<Flux<String>, Flux<String>> filterMap = name ->
                    name.map(String::toUpperCase)
                    .filter(s -> s.length() > length)
                    .flatMap(s -> splitString(s));

            var defaultFlux = Flux.just("default")
                    .transform(filterMap); // D,E,F,A,U,L,T

            return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                    .transform(filterMap)
                    .switchIfEmpty(defaultFlux)
                    .log();
        }

        // private ------------------------------------------------------------
        private Flux<String> splitString(String name) {
            var array = name.split(""); // Each character in the name
            return Flux.fromArray(array);
        }

        private Flux<String> splitStringWithDelay(String name) {
            var array = name.split(""); // Each character in the name
            var delayTime = new Random().nextInt(1000);
            return Flux.fromArray(array)
                    .delayElements(Duration.ofMillis(delayTime));
        }

        private Mono<List<String>> splitStringMono(String s) {
            var charArray = s.split("");
            var list = List.of(charArray); // A,L,E,X
            return Mono.just(list);
        }

    // main ------------------------------------------------------------
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
