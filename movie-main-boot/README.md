### Movie-main-boot Module(Web Client)

#### 1. info 
webflux 모듈들과 상호작용하여 Client 에게 완벽한 movie 데이터를 제공하는 모듈
이미 DB 와 상호작용하는 모듈2개(info, review) 가 존재하기에 DB Connection 은 존재하지 않음.

#### 2 Flow(동작흐름 및 개요)
1. Client 에게서 movieId 가 넘어온다면, `movie-info-boot` 로 부터 영화정보 가져온다.
2. 동시에 `movie-review-boot` 로 부터 영화평가 정보를 가져온다.


#### 3. 핵심사항

`WebClient` 를 이용하여 `movie-info-boot` 과 `movie-review-boot` 와 상호작용한다.<br/>
`WebClient` 는 비동기적으로 동작하며, `Mono` 와 `Flux` 를 이용하여 데이터를 처리한다.
- Non-blocking I/O
- Rest Client
- API 스타일로 상호작용
- Streaming 지원