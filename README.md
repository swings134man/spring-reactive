# Spring-Reactive 

### Info
- This project is about reactive programming with Spring
  - Using Spring WebFlux
  - netty ...

GitHub Repo Renamed at 2024-10-07 [netty -> spring-reactive]

### Modules Info

1. basic-reactor-boot: reactor-core 를 사용한 Reactive Programming 기본 모듈
2. movie-main-boot(MSA): WebFlux Test module(하위 2개의 모듈과 상호작용하여 완벽한 movie 데이터 제공)
3. movie-info-boot(MSA): WebFlux Test module(영화 정보)
   - mongoDB
   - Annotated Controller (WebFlux)
4. movie-review-boot(MSA): WebFlux Test module(영화 평가, 등급 정보)
   - mongoDB
   - Functional Endpoints (WebFlux)