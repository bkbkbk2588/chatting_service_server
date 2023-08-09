package com.example.chatting_server.vo.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenVo {
    private String accessToken;
    private String refreshToken;
}
