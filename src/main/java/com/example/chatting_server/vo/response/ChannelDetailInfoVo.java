package com.example.chatting_server.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDetailInfoVo {
    private String channelUrl;
    private LocalDateTime createTime;
    private String ownerNickname;
    private String channelName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Map<String, Object> channelMetadata;
    private String channelMetadataId;
    private List<String> members;
}
