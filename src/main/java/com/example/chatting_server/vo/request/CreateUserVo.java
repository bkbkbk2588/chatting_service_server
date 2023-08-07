package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CreateUserVo {

    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private String pw;

    @NotNull
    @NotBlank
    private String nickName;
}
