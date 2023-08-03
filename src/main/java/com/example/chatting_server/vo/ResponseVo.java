package com.example.chatting_server.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseVo {
    private int code;
    private String message;
}
