package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
public class ChannelMetadataInfoVo {
    private String channelMetadataId;
    private String channelUrl;
    private Map<String, Object> metadata;
}
