package com.example.chatting_server.vo.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class UserMetadataVo {
    private Map<String, Object> metadata;
    private String userId;
}
