package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RequestFriendVo {
    @NotNull
    @NotBlank
    private String nickName;
}
