package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelAndMetadataInfoVo {
    private String channelMetadataId;
    private String channelUrl;
    private String userPkId;
}
