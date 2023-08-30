package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChannelActiveUserVo {
    private String channelsUserId;
    private String channelUrl;
    private String userId;
    private int userState;
    private String nickName;
}
