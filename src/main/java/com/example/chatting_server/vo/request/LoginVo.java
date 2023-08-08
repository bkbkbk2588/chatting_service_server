package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class LoginVo {

    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private String pw;
}
