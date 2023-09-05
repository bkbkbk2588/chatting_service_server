package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ChannelInfoListVo {
    private String channelUrl;
    private LocalDateTime createTime;
    private String ownerNickname;
    private String channelName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Map<String, Object> channelMetadata;
    private String channelMetadataId;
    private int memberCount;
}
