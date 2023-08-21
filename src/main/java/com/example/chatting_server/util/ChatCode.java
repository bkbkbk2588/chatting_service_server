package com.example.chatting_server.util;

import lombok.Getter;

@Getter
public enum ChatCode {
    INVITE_WAIT(0),
    INVITE_ACCEPT(1),

    USER_OK(0),
    USER_SUSPENSION(1),
    USER_HUMAN(2);

    private final int code;

    ChatCode(int code){
        this.code = code;
    }
}
