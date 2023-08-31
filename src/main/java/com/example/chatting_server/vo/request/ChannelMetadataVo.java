package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
public class ChannelMetadataVo {
    @NotNull
    @NotBlank
    private String channelUrl;

    @NotNull
    private Map<String, Object> metadata;
}
