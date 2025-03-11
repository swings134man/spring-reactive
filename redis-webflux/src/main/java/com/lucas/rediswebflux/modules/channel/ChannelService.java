package com.lucas.rediswebflux.modules.channel;

import com.lucas.rediswebflux.common.redis.RedisChannelSubListener;
import com.lucas.rediswebflux.common.redis.RedisPublisher;
import com.lucas.rediswebflux.modules.channel.domain.ChannelDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @package : com.lucas.rediswebflux.modules.channel
 * @name : ChannelService.java
 * @date : 2025. 3. 11. 오후 6:04
 * @author : lucaskang(swings134man)
 * @Description: Redis Topic 생성 및, 메시지 전송 Service
 * - MSg 전송의 경우, Client 에서 정의된 프로토콜 타입을 받아 실행됨.(ChannelDTO)
**/
@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelService {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer container;
    private final RedisPublisher publisher;
    private final RedisChannelSubListener listener;

    private Map<String, ChannelTopic> topics = new ConcurrentHashMap<>();

    private static final String CHANNEL_PREFIX = "CHANNEL";

    /**
    * @methodName : initTopicsFromRedis
    * @date : 2025. 3. 10. 오후 6:57
    * @author : lucaskang
    * @Description: Init Channel From Redis
    **/
    @PostConstruct
    public void initTopicsFromRedis() {
        redisTemplate.opsForHash().entries(CHANNEL_PREFIX)
                .doOnNext(entry -> {
                    String channelId = (String) entry.getKey();
                    ChannelTopic topic = new ChannelTopic(channelId);
                    topics.put(channelId, topic);
                    container.addMessageListener(listener, topic);
                    log.info("🔄 Redis Channel Recovery: {}", channelId);
                })
                .blockLast();
    }

    /**
    * @methodName : msgSend
    * @date : 2025. 3. 10. 오후 6:55
    * @author : lucaskang
    * @Description: Send Message To redis Channel
    **/
    public void pushMsg(String channelId, ChannelDto message) {
        // 1. Channel info Get
        ChannelTopic channelTopic = topics.get(channelId);
        if(channelTopic == null) {
            log.error("Channel Not Found: {}", channelId);
            // TODO: Exception 처리
        }else {
            publisher.publish(channelTopic, message).subscribe();
        }
    }// msg send



    public Mono<String> newChannel(String channelId) {
        ChannelTopic channelTopic = topics.get(channelId);
        if(channelTopic == null) {
            channelTopic = new ChannelTopic(channelId);
            container.addMessageListener(listener, channelTopic);
            topics.put(channelId, channelTopic);
            log.info("New Channel Created: {}", channelId);
        }
        return Mono.just(channelId);
    }

    public Mono<String> createChannelByName(String channelName) {
        String channelId = UUID.randomUUID().toString();
        ChannelTopic topic = new ChannelTopic(channelId); // ChannelTopic 생성
        topics.put(channelId, topic);  // topics 저장
        return redisTemplate.opsForHash().put(CHANNEL_PREFIX, channelId, channelName)
                .then(Mono.just(channelId));
    }

    public void findChannel(String channelId) {
        redisTemplate.opsForHash().get(CHANNEL_PREFIX, channelId);
    }

}
