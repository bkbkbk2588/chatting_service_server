package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChannelInfoResultVo {
    private String channelUrl;
    private LocalDateTime createTime;
    private String ownerNickname;
    private String channelName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private String channelMetadata;
    private String channelMetadataId;
}
