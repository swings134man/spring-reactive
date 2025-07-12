# kotlin-co-flux module (Kotlin Coroutine + Spring WebFlux)

- Kotlin Coroutine 을 사용하여 Spring WebFlux 적용한 Sample Module 
  - 기존의 mono/flux 방식이 아닌 `Coroutine` 방식으로 Callback 처리방식과, 가독성, WebFlux 코드 작성에 대해서 개선한 프로젝트


---

### Stack 
- Spring boot 3.4.7
- WebFlux
- Kotlin 1.9.25
- postgresql 17-alpine 

---

### Info 
- 해당 모듈은 Kotlin Coroutine 기반으로 WebFlux 를 사용한 예제
- `spring-essentials` 내부의 `kopring-jpa-demo` 모듈에서 사용된 `Model` 을 참고하여 샘플 제작되었음.
  - 테스트하면서 생성해둔 DB(postgresql) 및 table 구조는 동일하게 사용된다

  
--- 
### TODO LIST
- board 모델
  - Paging API 추가
  - Custom Repository 추가 하여 사용자 정의 Query 생성 및 테스트
    - 다건, 단건 조회 - 상황별 쿼리(limit, where, order by 등등)