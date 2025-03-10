package com.lucas.rediswebflux.modules.channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
