package com.lucas.rediswebflux.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.rediswebflux.modules.channel.ChannelService;
import com.lucas.rediswebflux.modules.channel.domain.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisChannelSubListener implements MessageListener {

    private final ReactiveRedisTemplate<String, Object> template;
    private final ObjectMapper mapper;
//    private final ChannelService channelService;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            ChannelDto channelDto = mapper.readValue(msg, ChannelDto.class);
            String channelId = channelDto.getChannelId();

            // send To Service Class
            log.info("channelId:{}, msg:{}", channelId, channelDto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
