package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$")
    private String phoneNumber;
}
