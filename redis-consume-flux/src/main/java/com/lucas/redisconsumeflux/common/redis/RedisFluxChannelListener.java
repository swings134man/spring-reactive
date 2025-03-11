package com.lucas.redisconsumeflux.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.redisconsumeflux.modules.flux.FluxChannelDTO;
import com.lucas.redisconsumeflux.modules.flux.MsgHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @package : com.lucas.redisconsumeflux.common.redis
 * @name : RedisFluxChannelListener.java
 * @date : 2025. 3. 11. 오후 5:03
 * @author : lucaskang(swings134man)
 * @Description: Redis 의 Flux Channel 에 대한 Subscribe Listener
**/
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisFluxChannelListener implements MessageListener {

    private final ReactiveRedisTemplate<String, Object> template;
    private final ObjectMapper mapper;
    private final MsgHandlerService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            FluxChannelDTO dto = mapper.readValue(msg, FluxChannelDTO.class);

            log.info("Flux Channel Subs : {} ", dto);
            service.handleMsg(dto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
