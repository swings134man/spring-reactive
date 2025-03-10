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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
        Flux<Map.Entry<Object, Object>> entries = redisTemplate.opsForHash().entries(CHANNEL_PREFIX);

        for (Object key : entries.toIterable()) {
            String channelId = (String) key;
            ChannelTopic topic = new ChannelTopic(channelId);
            topics.put(channelId, topic);
            container.addMessageListener(listener, topic);
            log.info("🔄 Redis Channel Recovery: {}", channelId);
        }
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
            publisher.publish(channelTopic, message);
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
        return redisTemplate.opsForHash().put(CHANNEL_PREFIX, channelId, channelName)
                .then(newChannel(channelId));
    }


}
