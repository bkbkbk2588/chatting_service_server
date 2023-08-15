package com.example.chatting_server.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoVo {

    private String userId;

    private String nickName;

    private int userStatus;

    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String metaData;
}
