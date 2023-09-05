package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ChannelMetadataIdVo {
    @NotNull
    @NotBlank
    private String channelUrl;
}
