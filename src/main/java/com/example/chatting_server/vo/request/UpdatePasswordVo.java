package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class UpdatePasswordVo {

    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private String newPw;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$")
    private String phoneNumber;
}
