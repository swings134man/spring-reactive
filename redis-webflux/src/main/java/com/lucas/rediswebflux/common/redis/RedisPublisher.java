package com.lucas.rediswebflux.common.redis;

import com.lucas.rediswebflux.modules.channel.domain.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final ReactiveRedisTemplate<String, Object> template;

    /**
     * Object publish
     * @return Mono<Void>
     */
    public Mono<Void> publish(ChannelTopic topic, ChannelDto dto) {
        return template.convertAndSend(topic.getTopic(), dto)
                .doOnNext(count -> log.info("📢 Redis Published to [{}] - Subscribers: {}", topic.getTopic(), count))
                .then(); // Mono<Void> 반환하여 비동기적으로 실행
    }

    /**
     * String publish
     * @return Mono<Void>
     */
    public Mono<Void> publish(ChannelTopic topic , String data) {
        return template.convertAndSend(topic.getTopic(), data)
                .then();
    }
}
