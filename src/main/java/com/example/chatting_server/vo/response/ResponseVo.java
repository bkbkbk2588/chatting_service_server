package com.example.chatting_server.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseVo {
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
}
