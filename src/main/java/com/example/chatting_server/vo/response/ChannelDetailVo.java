package com.example.chatting_server.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDetailVo {
    private String channelUrl;
    private LocalDateTime createTime;
    private String ownerNickname;
    private String channelName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private String channelMetadata;
    private String channelMetadataId;
    private String nickname;
}
