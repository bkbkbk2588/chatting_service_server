package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateUserVo {

    @NotBlank
    private String id;

    @NotBlank
    private String pw;

    @NotBlank
    private String nickName;
}
