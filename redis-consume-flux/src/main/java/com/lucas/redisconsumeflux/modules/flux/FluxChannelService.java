package com.lucas.redisconsumeflux.modules.flux;

import com.lucas.redisconsumeflux.common.redis.RedisFluxChannelListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
//953c8b7b-377c-45a7-8381-cdd276455e79 : flux(Sample)
//aedf5955-f092-4764-a658-d0506b17ebf2 : flux_two(Sample)
public class FluxChannelService {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer container;
    private final RedisFluxChannelListener listener;

    private static final String CHANNEL_PREFIX = "CHANNEL";

    @PostConstruct
    public void initTopicsFromRedis() {
        redisTemplate.opsForHash().entries(CHANNEL_PREFIX)
                .doOnNext(entry -> {
                    String channelId = (String) entry.getKey();
                    ChannelTopic topic = new ChannelTopic(channelId);
                    container.addMessageListener(listener, topic);
                    log.info("🔄 Redis Channel Recovery: {}", channelId);
                })
                .blockLast();
    }


}
