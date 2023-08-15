package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UpdateUserVo {

    private String nickName;

    @Pattern(regexp = "^\\d{10,11}$")
    private String phoneNumber;
}
