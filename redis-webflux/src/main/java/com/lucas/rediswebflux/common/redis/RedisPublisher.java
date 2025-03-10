package com.lucas.rediswebflux.common.redis;

import com.lucas.rediswebflux.modules.channel.domain.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    public RedisPublisher(RedisTemplate<String, Object> template) {
        this.template = template;
    }


    /**
     * Object publish
     */
    public void publish(ChannelTopic topic, ChannelDto dto) {
        template.convertAndSend(topic.getTopic(), dto);
    }

    /**
     * String publish
     */
    public void publish(ChannelTopic topic ,String data) {
        template.convertAndSend(topic.getTopic(), data);
    }
}
