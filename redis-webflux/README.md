## redis-webflux 모듈

- WebFlux를 사용하여 Redis를 사용하는 예제 프로젝트입니다.

### Info
- Caching 기능
- 실시간 채팅 처리(redis pub/sub)
- 대기열 처리 기능

---
### Redis Needs To Pub/sub Classes

> #### Redis Pub/Sub
> - RedisPublisher : Redis에 메시지를 발행하는 클래스(ChannelTopic 을 사용하여, 어디에 보낼건지, 어떤 메시지를 보낼건지)
> - RedisSubscriberListener : Redis에 메시지를 구독하는 클래스 - 구독자가 메시지를 받을 때 처리할 로직을 정의(pub 이 되었을때)
> 

> ### Sample Flow
> 1. Redis 에 특정 Channel 생성
> 2. Server(Client) 가 특정 API 를 통해, 해당 Channel 에 메시지 발행
> 3. Subscriber 가 해당 Channel 을 구독하고, 메시지를 받음 (MessageListener)
> #### Client
> 4. 특정 Channel 을 SSE 로 구독하여, 메시지를 받음







