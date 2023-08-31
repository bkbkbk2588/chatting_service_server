package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class InviteChannelVo {
    private String channelUserId;
    private String channelUrl;
    private int state;
}
