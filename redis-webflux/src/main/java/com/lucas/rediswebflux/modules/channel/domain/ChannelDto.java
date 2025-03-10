package com.lucas.rediswebflux.modules.channel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String channelId; // 채널 ID
    private String channelName; // 채널 이름
    private String message; // 실 메시지
    private String sender; // 전송자
    private String type; // <?>

}
