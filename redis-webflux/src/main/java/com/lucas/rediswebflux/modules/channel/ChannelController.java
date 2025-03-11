package com.lucas.rediswebflux.modules.channel;

import com.lucas.rediswebflux.modules.channel.domain.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/{channelName}")
    public Mono<ResponseEntity<String>> createChannel(@PathVariable String channelName) {
        return channelService.createChannelByName(channelName)
                .map(data -> ResponseEntity.ok().body(data));
    }

    @PostMapping("/flux/pub")
    public void sendMsg(@RequestBody ChannelDto dto) {
        channelService.pushMsg(dto.getChannelId() ,dto);
    }
}
