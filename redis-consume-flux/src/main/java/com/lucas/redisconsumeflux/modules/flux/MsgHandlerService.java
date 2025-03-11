package com.lucas.redisconsumeflux.modules.flux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @package : com.lucas.redisconsumeflux.modules.flux
 * @name : MsgHandlerService.java
 * @date : 2025. 3. 11. 오후 5:54
 * @author : lucaskang(swings134man)
 * @Description: Redis Flux Channel 에 발행된 메시지 처리 샘플 Service Class
**/
@Service
@RequiredArgsConstructor
@Slf4j
public class MsgHandlerService {

    public void handleMsg(FluxChannelDTO message) {
        log.info("---------- Handle Service Class in ----------");
        log.info("msg : {}", message);
        log.info("---------- Handle Service Class out ----------");
    }
}
