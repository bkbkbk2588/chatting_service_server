package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
public class CreateUserMetadataVo {
    @NotNull
    private Map<String, Object> metadata;
}
