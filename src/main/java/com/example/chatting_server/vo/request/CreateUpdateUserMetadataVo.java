package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
public class CreateUpdateUserMetadataVo {
    @NotNull
    private Map<String, Object> metadata;
}
