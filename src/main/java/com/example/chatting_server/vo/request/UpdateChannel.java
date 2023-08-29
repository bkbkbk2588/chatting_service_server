package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
public class UpdateChannel {

    @NotNull
    @NotBlank
    private String channelUrl;
    private Map<String, Object> metadata;
    private String channelName;
}
