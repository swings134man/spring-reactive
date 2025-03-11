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
5. r2dbc-maria-flux: Webflux + R2DBC + MariaDB 연동 모듈(Reactive Connection)
   - MariaDB
   - R2DBC
   - Annotated Controller (WebFlux)
6. redis-webflux Webflux 에서 Reactive Redis 연동 모듈
   - Reactivce Redis
   - Annotated Controller (WebFlux)
7. redis-consume-flux : redis-webflux 에서 Redis Pub/Sub 을 이용한 데이터 소비 모듈
   - Reactivce Redis
   - Annotated Controller (WebFlux)